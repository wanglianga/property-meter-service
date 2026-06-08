package com.property.meter.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.meter.common.BusinessException;
import com.property.meter.dto.BillGenerateDTO;
import com.property.meter.dto.BillPaymentDTO;
import com.property.meter.entity.*;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.ReadingStatus;
import com.property.meter.entity.enums.SharingType;
import com.property.meter.repository.BillRepository;
import com.property.meter.repository.MeterReadingRepository;
import com.property.meter.repository.PublicMeterReadingRepository;
import com.property.meter.repository.SharingRuleRepository;
import com.property.meter.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final PublicMeterReadingRepository publicMeterReadingRepository;
    private final SharingRuleRepository sharingRuleRepository;
    private final RoomService roomService;
    private final BuildingService buildingService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public List<Bill> generateBills(BillGenerateDTO dto) {
        log.info("开始生成账单: period={}, buildingId={}, meterType={}", dto.getPeriod(), dto.getBuildingId(), dto.getMeterType());

        List<Room> rooms;
        if (dto.getBuildingId() != null) {
            rooms = roomService.listByBuilding(dto.getBuildingId());
        } else {
            rooms = roomService.listAll();
        }

        List<MeterType> meterTypes = dto.getMeterType() != null
                ? List.of(dto.getMeterType())
                : List.of(MeterType.WATER, MeterType.ELECTRICITY);

        List<Bill> generatedBills = new ArrayList<>();

        for (Room room : rooms) {
            for (MeterType meterType : meterTypes) {
                if (billRepository.existsByRoomIdAndPeriodAndMeterType(room.getId(), dto.getPeriod(), meterType)) {
                    log.info("账单已存在，跳过: roomId={}, period={}, type={}", room.getId(), dto.getPeriod(), meterType);
                    continue;
                }
                try {
                    Bill bill = generateSingleBill(room, dto.getPeriod(), meterType);
                    if (bill != null) {
                        generatedBills.add(bill);
                    }
                } catch (Exception e) {
                    log.error("生成账单失败: roomId={}, type={}", room.getId(), meterType, e);
                }
            }
        }

        log.info("账单生成完成，共生成 {} 条", generatedBills.size());
        return generatedBills;
    }

    private Bill generateSingleBill(Room room, String period, MeterType meterType) {
        Bill bill = new Bill();
        bill.setBillNo(generateBillNo(period, meterType, room.getId()));
        bill.setRoom(room);
        bill.setPeriod(period);
        bill.setMeterType(meterType);
        bill.setStatus(BillStatus.UNPAID);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setIsLocked(false);

        List<MeterReading> readings = meterReadingRepository.findByRoomIdAndPeriod(room.getId(), period);
        MeterReading personalReading = readings.stream()
                .filter(r -> r.getMeter().getMeterType() == meterType)
                .findFirst()
                .orElse(null);

        BigDecimal personalUsage = BigDecimal.ZERO;
        BigDecimal personalAmount = BigDecimal.ZERO;
        BigDecimal unitPrice = getDefaultUnitPrice(meterType);

        if (personalReading != null && personalReading.getStatus() != ReadingStatus.MISSING) {
            if (Boolean.TRUE.equals(room.getIsVacant())) {
                personalUsage = BigDecimal.ZERO;
            } else {
                personalUsage = personalReading.getActualUsage() != null ? personalReading.getActualUsage() : BigDecimal.ZERO;
            }
            SharingRule rule = findPersonalRule(room, meterType);
            if (rule != null && rule.getUnitPrice() != null) {
                unitPrice = rule.getUnitPrice();
            }
            personalAmount = personalUsage.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);
        }

        bill.setPersonalUsage(personalUsage);
        bill.setPersonalAmount(personalAmount);

        SharingResult sharingResult = calculateSharing(room, period, meterType);
        bill.setSharedUsage(sharingResult.usage);
        bill.setSharedAmount(sharingResult.amount);
        if (sharingResult.unitPrice != null) {
            unitPrice = sharingResult.unitPrice;
        }

        bill.setTotalUsage(personalUsage.add(sharingResult.usage));
        bill.setTotalAmount(personalAmount.add(sharingResult.amount));
        bill.setUnitPrice(unitPrice);

        try {
            bill.setSharingDetail(objectMapper.writeValueAsString(sharingResult.detail));
        } catch (JsonProcessingException e) {
            bill.setSharingDetail("[]");
        }

        return billRepository.save(bill);
    }

    private SharingResult calculateSharing(Room room, String period, MeterType meterType) {
        SharingResult result = new SharingResult();
        result.usage = BigDecimal.ZERO;
        result.amount = BigDecimal.ZERO;
        result.detail = new ArrayList<>();

        Building building = room.getBuilding();
        List<SharingRule> rules = findApplicableRules(building, meterType);

        for (SharingRule rule : rules) {
            SharingRuleDetail detail = new SharingRuleDetail();
            detail.ruleId = rule.getId();
            detail.ruleName = rule.getRuleName();
            detail.sharingType = rule.getSharingType().name();

            PublicMeter publicMeter = rule.getPublicMeter();
            BigDecimal totalUsage = BigDecimal.ZERO;
            if (publicMeter != null) {
                PublicMeterReading pmReading = publicMeterReadingRepository
                        .findByPublicMeterIdAndPeriod(publicMeter.getId(), period).orElse(null);
                if (pmReading != null && pmReading.getActualUsage() != null) {
                    totalUsage = pmReading.getActualUsage();
                }
            }

            BigDecimal roomShareRatio = calculateShareRatio(room, rule);
            BigDecimal roomUsage = totalUsage.multiply(roomShareRatio).setScale(4, RoundingMode.HALF_UP);
            BigDecimal price = rule.getUnitPrice() != null ? rule.getUnitPrice() : getDefaultUnitPrice(meterType);
            BigDecimal roomAmount = roomUsage.multiply(price).setScale(2, RoundingMode.HALF_UP);

            if (rule.getFixedAmount() != null) {
                roomAmount = roomAmount.add(rule.getFixedAmount());
            }

            detail.totalUsage = totalUsage;
            detail.shareRatio = roomShareRatio;
            detail.roomUsage = roomUsage;
            detail.unitPrice = price;
            detail.fixedAmount = rule.getFixedAmount();
            detail.roomAmount = roomAmount;

            result.usage = result.usage.add(roomUsage);
            result.amount = result.amount.add(roomAmount);
            result.unitPrice = price;
            result.detail.add(detail);
        }

        return result;
    }

    private List<SharingRule> findApplicableRules(Building building, MeterType meterType) {
        List<SharingRule> rules = new ArrayList<>();

        List<SharingRule> buildingRules = sharingRuleRepository.findByBuildingIdAndMeterType(building.getId(), meterType);
        rules.addAll(buildingRules.stream().filter(SharingRule::getIsActive).toList());

        if (rules.isEmpty()) {
            List<SharingRule> allRules = sharingRuleRepository.findByMeterType(meterType);
            rules.addAll(allRules.stream()
                    .filter(r -> Boolean.TRUE.equals(r.getIsActive()) && r.getBuilding() == null)
                    .toList());
        }

        return rules;
    }

    private SharingRule findPersonalRule(Room room, MeterType meterType) {
        List<SharingRule> rules = sharingRuleRepository.findByBuildingIdAndMeterType(room.getBuilding().getId(), meterType);
        return rules.stream().filter(SharingRule::getIsActive).findFirst().orElse(null);
    }

    private BigDecimal calculateShareRatio(Room room, SharingRule rule) {
        SharingType type = rule.getSharingType();
        return switch (type) {
            case BY_AREA -> calculateByArea(room, rule);
            case BY_HOUSEHOLD -> calculateByHousehold(room, rule);
            case BY_BUILDING -> calculateByBuilding(room, rule);
            case CUSTOM -> new BigDecimal("0.1");
        };
    }

    private BigDecimal calculateByArea(Room room, SharingRule rule) {
        List<Room> rooms = rule.getBuilding() != null
                ? roomService.listByBuilding(rule.getBuilding().getId())
                : roomService.listAll();
        BigDecimal totalArea = rooms.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsVacant()))
                .map(Room::getSharingArea)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalArea.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal roomArea = room.getSharingArea() != null ? room.getSharingArea() : BigDecimal.ZERO;
        return roomArea.divide(totalArea, 6, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateByHousehold(Room room, SharingRule rule) {
        List<Room> rooms = rule.getBuilding() != null
                ? roomService.listByBuilding(rule.getBuilding().getId())
                : roomService.listAll();
        int totalHouseholds = rooms.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsVacant()))
                .mapToInt(r -> r.getHouseholdCount() != null ? r.getHouseholdCount() : 1)
                .sum();
        if (totalHouseholds <= 0) {
            return BigDecimal.ZERO;
        }
        int roomHouseholds = room.getHouseholdCount() != null ? room.getHouseholdCount() : 1;
        return new BigDecimal(roomHouseholds).divide(new BigDecimal(totalHouseholds), 6, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateByBuilding(Room room, SharingRule rule) {
        List<Room> rooms = rule.getBuilding() != null
                ? roomService.listByBuilding(rule.getBuilding().getId())
                : roomService.listAll();
        long validRooms = rooms.stream().filter(r -> !Boolean.TRUE.equals(r.getIsVacant())).count();
        if (validRooms <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ONE.divide(new BigDecimal(validRooms), 6, RoundingMode.HALF_UP);
    }

    private BigDecimal getDefaultUnitPrice(MeterType type) {
        return type == MeterType.WATER ? new BigDecimal("4.5") : new BigDecimal("0.6");
    }

    private String generateBillNo(String period, MeterType type, Long roomId) {
        String prefix = type == MeterType.WATER ? "W" : "E";
        return prefix + period.replace("-", "") + String.format("%06d", roomId)
                + DateTimeFormatter.ofPattern("HHmmss").format(LocalDateTime.now());
    }

    @Override
    public Bill getById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BusinessException("账单不存在, id: " + id));
    }

    @Override
    public Bill getByBillNo(String billNo) {
        return billRepository.findByBillNo(billNo)
                .orElseThrow(() -> new BusinessException("账单不存在, billNo: " + billNo));
    }

    @Override
    public List<Bill> listByRoom(Long roomId) {
        return billRepository.findByRoomId(roomId);
    }

    @Override
    public List<Bill> listByRoomAndPeriod(Long roomId, String period) {
        return billRepository.findByRoomIdAndPeriod(roomId, period);
    }

    @Override
    public List<Bill> listByPeriod(String period) {
        return billRepository.findByPeriod(period);
    }

    @Override
    public List<Bill> listByStatus(BillStatus status) {
        return billRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Bill payBill(BillPaymentDTO dto) {
        Bill bill = getById(dto.getBillId());

        if (bill.getStatus() == BillStatus.PAID) {
            throw new BusinessException("账单已缴费，不可重复支付");
        }
        if (Boolean.TRUE.equals(bill.getIsLocked())) {
            throw new BusinessException("账单已锁定，不可操作");
        }

        bill.setStatus(BillStatus.PAID);
        bill.setPaidTime(LocalDateTime.now());
        bill.setPayer(dto.getPayer());
        bill.setPaymentMethod(dto.getPaymentMethod());
        bill.setPaymentRef(dto.getPaymentRef());
        bill.setIsLocked(true);

        log.info("账单支付成功: billNo={}, amount={}", bill.getBillNo(), bill.getTotalAmount());
        return billRepository.save(bill);
    }

    @Override
    public List<Bill> listUnpaidByRoom(Long roomId) {
        return billRepository.findByRoomId(roomId).stream()
                .filter(b -> b.getStatus() != BillStatus.PAID)
                .toList();
    }

    private static class SharingResult {
        BigDecimal usage;
        BigDecimal amount;
        BigDecimal unitPrice;
        List<SharingRuleDetail> detail;
    }

    private static class SharingRuleDetail {
        public Long ruleId;
        public String ruleName;
        public String sharingType;
        public BigDecimal totalUsage;
        public BigDecimal shareRatio;
        public BigDecimal roomUsage;
        public BigDecimal unitPrice;
        public BigDecimal fixedAmount;
        public BigDecimal roomAmount;
    }
}
