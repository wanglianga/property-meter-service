package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.PublicMeterDTO;
import com.property.meter.dto.PublicMeterReadingDTO;
import com.property.meter.entity.Building;
import com.property.meter.entity.PublicMeter;
import com.property.meter.entity.PublicMeterReading;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.ReadingStatus;
import com.property.meter.repository.PublicMeterReadingRepository;
import com.property.meter.repository.PublicMeterRepository;
import com.property.meter.service.BuildingService;
import com.property.meter.service.PublicMeterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicMeterServiceImpl implements PublicMeterService {

    private final PublicMeterRepository publicMeterRepository;
    private final PublicMeterReadingRepository publicMeterReadingRepository;
    private final BuildingService buildingService;

    private static final BigDecimal ABNORMAL_THRESHOLD = new BigDecimal("2.0");

    @Override
    @Transactional
    public PublicMeter createMeter(PublicMeterDTO dto) {
        publicMeterRepository.findByMeterCode(dto.getMeterCode())
                .ifPresent(m -> {
                    throw new BusinessException("公区表编号已存在: " + dto.getMeterCode());
                });
        PublicMeter meter = new PublicMeter();
        if (dto.getBuildingId() != null) {
            Building building = buildingService.getById(dto.getBuildingId());
            meter.setBuilding(building);
        }
        meter.setMeterCode(dto.getMeterCode());
        meter.setMeterName(dto.getMeterName());
        meter.setMeterType(dto.getMeterType());
        meter.setMeterBrand(dto.getMeterBrand());
        meter.setMeterModel(dto.getMeterModel());
        meter.setMultiplier(dto.getMultiplier() != null ? dto.getMultiplier() : BigDecimal.ONE);
        meter.setLastReading(dto.getLastReading() != null ? dto.getLastReading() : BigDecimal.ZERO);
        meter.setInstallLocation(dto.getInstallLocation());
        meter.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        meter.setRemark(dto.getRemark());
        return publicMeterRepository.save(meter);
    }

    @Override
    @Transactional
    public PublicMeter updateMeter(Long id, PublicMeterDTO dto) {
        PublicMeter meter = getMeterById(id);
        if (!meter.getMeterCode().equals(dto.getMeterCode())) {
            publicMeterRepository.findByMeterCode(dto.getMeterCode())
                    .ifPresent(m -> {
                        throw new BusinessException("公区表编号已存在: " + dto.getMeterCode());
                    });
        }
        if (dto.getBuildingId() != null && (meter.getBuilding() == null || !meter.getBuilding().getId().equals(dto.getBuildingId()))) {
            Building building = buildingService.getById(dto.getBuildingId());
            meter.setBuilding(building);
        }
        meter.setMeterCode(dto.getMeterCode());
        meter.setMeterName(dto.getMeterName());
        meter.setMeterType(dto.getMeterType());
        meter.setMeterBrand(dto.getMeterBrand());
        meter.setMeterModel(dto.getMeterModel());
        meter.setMultiplier(dto.getMultiplier() != null ? dto.getMultiplier() : meter.getMultiplier());
        if (dto.getLastReading() != null) {
            meter.setLastReading(dto.getLastReading());
        }
        meter.setInstallLocation(dto.getInstallLocation());
        meter.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : meter.getIsActive());
        meter.setRemark(dto.getRemark());
        return publicMeterRepository.save(meter);
    }

    @Override
    @Transactional
    public void deleteMeter(Long id) {
        PublicMeter meter = getMeterById(id);
        publicMeterRepository.delete(meter);
    }

    @Override
    public PublicMeter getMeterById(Long id) {
        return publicMeterRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公区表不存在, id: " + id));
    }

    @Override
    public PublicMeter getMeterByCode(String code) {
        return publicMeterRepository.findByMeterCode(code)
                .orElseThrow(() -> new BusinessException("公区表不存在, code: " + code));
    }

    @Override
    public List<PublicMeter> listMetersByBuilding(Long buildingId) {
        return publicMeterRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<PublicMeter> listMetersByBuildingAndType(Long buildingId, MeterType type) {
        return publicMeterRepository.findByBuildingIdAndMeterType(buildingId, type);
    }

    @Override
    public List<PublicMeter> listAllActiveMeters() {
        return publicMeterRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional
    public PublicMeterReading createReading(PublicMeterReadingDTO dto) {
        PublicMeter meter = getMeterById(dto.getPublicMeterId());

        publicMeterReadingRepository.findByPublicMeterIdAndPeriod(dto.getPublicMeterId(), dto.getPeriod())
                .ifPresent(r -> {
                    throw new BusinessException("该公区表本期已抄录");
                });

        BigDecimal previousReading = dto.getPreviousReading() != null ? dto.getPreviousReading() : meter.getLastReading();
        BigDecimal currentReading = dto.getCurrentReading();
        BigDecimal multiplier = dto.getMultiplier() != null ? dto.getMultiplier() : meter.getMultiplier();

        PublicMeterReading reading = new PublicMeterReading();
        reading.setPublicMeter(meter);
        reading.setPeriod(dto.getPeriod());
        reading.setReadingDate(LocalDate.now());
        reading.setPreviousReading(previousReading);
        reading.setCurrentReading(currentReading);
        reading.setMultiplier(multiplier);
        reading.setPhotoUrl(dto.getPhotoUrl());
        reading.setMeterReader(dto.getMeterReader());
        reading.setReadTime(LocalDateTime.now());
        reading.setRemark(dto.getRemark());

        validateAndSetStatus(reading, meter, previousReading, currentReading);

        BigDecimal usage = currentReading.subtract(previousReading).max(BigDecimal.ZERO).multiply(multiplier);
        reading.setActualUsage(usage);

        PublicMeterReading saved = publicMeterReadingRepository.save(reading);
        meter.setLastReading(currentReading);
        return saved;
    }

    @Override
    @Transactional
    public PublicMeterReading updateReading(Long id, PublicMeterReadingDTO dto) {
        PublicMeterReading reading = getReadingById(id);
        PublicMeter meter = reading.getPublicMeter();

        BigDecimal previousReading = dto.getPreviousReading() != null ? dto.getPreviousReading() : reading.getPreviousReading();
        BigDecimal currentReading = dto.getCurrentReading();
        BigDecimal multiplier = dto.getMultiplier() != null ? dto.getMultiplier() : reading.getMultiplier();

        reading.setPeriod(dto.getPeriod());
        reading.setPreviousReading(previousReading);
        reading.setCurrentReading(currentReading);
        reading.setMultiplier(multiplier);
        reading.setPhotoUrl(dto.getPhotoUrl());
        reading.setMeterReader(dto.getMeterReader());
        reading.setRemark(dto.getRemark());

        validateAndSetStatus(reading, meter, previousReading, currentReading);

        BigDecimal usage = currentReading.subtract(previousReading).max(BigDecimal.ZERO).multiply(multiplier);
        reading.setActualUsage(usage);

        return publicMeterReadingRepository.save(reading);
    }

    @Override
    @Transactional
    public void deleteReading(Long id) {
        PublicMeterReading reading = getReadingById(id);
        publicMeterReadingRepository.delete(reading);
    }

    @Override
    public PublicMeterReading getReadingById(Long id) {
        return publicMeterReadingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公区抄表记录不存在, id: " + id));
    }

    @Override
    public List<PublicMeterReading> listReadingsByMeter(Long publicMeterId) {
        return publicMeterReadingRepository.findByPublicMeterId(publicMeterId);
    }

    @Override
    public List<PublicMeterReading> listReadingsByPeriod(String period) {
        return publicMeterReadingRepository.findByPeriod(period);
    }

    @Override
    public PublicMeterReading getReadingByMeterAndPeriod(Long publicMeterId, String period) {
        return publicMeterReadingRepository.findByPublicMeterIdAndPeriod(publicMeterId, period)
                .orElseThrow(() -> new BusinessException("公区抄表记录不存在"));
    }

    private void validateAndSetStatus(PublicMeterReading reading, PublicMeter meter, BigDecimal previous, BigDecimal current) {
        if (current.compareTo(previous) < 0) {
            reading.setStatus(ReadingStatus.REVERSED);
            reading.setIsAbnormal(true);
            reading.setAbnormalReason("公区表倒走: 本期读数(" + current + ") < 上期读数(" + previous + ")");
            log.warn("公区表倒走: meterCode={}", meter.getMeterCode());
            return;
        }

        List<PublicMeterReading> history = publicMeterReadingRepository.findByPublicMeterId(meter.getId());
        if (history.size() >= 2) {
            BigDecimal avgUsage = history.stream()
                    .filter(h -> h.getActualUsage() != null && h.getActualUsage().compareTo(BigDecimal.ZERO) > 0)
                    .map(PublicMeterReading::getActualUsage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(history.size()), 4, BigDecimal.ROUND_HALF_UP);
            BigDecimal usage = current.subtract(previous);
            if (avgUsage.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal ratio = usage.divide(avgUsage, 4, BigDecimal.ROUND_HALF_UP);
                if (ratio.compareTo(ABNORMAL_THRESHOLD) > 0) {
                    reading.setStatus(ReadingStatus.ABNORMAL);
                    reading.setIsAbnormal(true);
                    reading.setAbnormalReason("公区用量异常: 本期(" + usage + ")超均值(" + avgUsage + ")的2倍, 比率=" + ratio);
                    log.warn("公区异常: meterCode={}, ratio={}", meter.getMeterCode(), ratio);
                    return;
                }
            }
        }

        reading.setStatus(ReadingStatus.NORMAL);
        reading.setIsAbnormal(false);
    }
}
