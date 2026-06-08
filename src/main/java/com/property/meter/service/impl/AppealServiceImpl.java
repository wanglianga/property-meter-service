package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.AppealDTO;
import com.property.meter.dto.AppealHandleDTO;
import com.property.meter.entity.Appeal;
import com.property.meter.entity.Bill;
import com.property.meter.entity.Room;
import com.property.meter.entity.enums.AppealStatus;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.repository.AppealRepository;
import com.property.meter.repository.BillRepository;
import com.property.meter.service.AppealService;
import com.property.meter.service.BillService;
import com.property.meter.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final BillRepository billRepository;
    private final BillService billService;
    private final RoomService roomService;

    @Override
    @Transactional
    public Appeal create(AppealDTO dto) {
        Bill bill = billService.getById(dto.getBillId());
        Room room = bill.getRoom();

        if (bill.getStatus() == BillStatus.PAID) {
            throw new BusinessException("已缴费账单不可发起申诉");
        }

        Appeal appeal = new Appeal();
        appeal.setAppealNo("AP" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        appeal.setBill(bill);
        appeal.setRoom(room);
        appeal.setAppellant(dto.getAppellant() != null ? dto.getAppellant() : room.getOwnerName());
        appeal.setAppellantPhone(dto.getAppellantPhone() != null ? dto.getAppellantPhone() : room.getOwnerPhone());
        appeal.setAppealType(dto.getAppealType());
        appeal.setDisputedUsage(dto.getDisputedUsage());
        appeal.setDisputedAmount(dto.getDisputedAmount());
        appeal.setAppealReason(dto.getAppealReason());
        appeal.setPhotoUrls(dto.getPhotoUrls());
        appeal.setStatus(AppealStatus.PENDING);
        appeal.setRemark(dto.getRemark());

        bill.setStatus(BillStatus.APPEALING);
        billRepository.save(bill);

        log.info("申诉创建成功: appealNo={}, billNo={}", appeal.getAppealNo(), bill.getBillNo());
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional
    public Appeal handle(AppealHandleDTO dto) {
        Appeal appeal = getById(dto.getAppealId());

        if (appeal.getStatus() == AppealStatus.APPROVED || appeal.getStatus() == AppealStatus.REJECTED) {
            throw new BusinessException("申诉已处理，不可重复处理");
        }

        appeal.setStatus(dto.getStatus());
        appeal.setHandlerOpinion(dto.getHandlerOpinion());
        appeal.setHandler(dto.getHandler());
        appeal.setHandleTime(LocalDateTime.now());

        Bill bill = appeal.getBill();
        if (dto.getStatus() == AppealStatus.APPROVED) {
            bill.setStatus(BillStatus.UNPAID);
            bill.setIsLocked(false);
            log.info("申诉通过，账单已重置为待缴费: billNo={}", bill.getBillNo());
        } else if (dto.getStatus() == AppealStatus.REJECTED) {
            bill.setStatus(BillStatus.UNPAID);
            log.info("申诉驳回，账单保持待缴费: billNo={}", bill.getBillNo());
        }
        billRepository.save(bill);

        log.info("申诉处理完成: appealNo={}, status={}", appeal.getAppealNo(), dto.getStatus());
        return appealRepository.save(appeal);
    }

    @Override
    public Appeal getById(Long id) {
        return appealRepository.findById(id)
                .orElseThrow(() -> new BusinessException("申诉不存在, id: " + id));
    }

    @Override
    public Appeal getByAppealNo(String appealNo) {
        return appealRepository.findByAppealNo(appealNo)
                .orElseThrow(() -> new BusinessException("申诉不存在, appealNo: " + appealNo));
    }

    @Override
    public List<Appeal> listByBill(Long billId) {
        return appealRepository.findByBillId(billId);
    }

    @Override
    public List<Appeal> listByRoom(Long roomId) {
        return appealRepository.findByRoomId(roomId);
    }

    @Override
    public List<Appeal> listByStatus(AppealStatus status) {
        return appealRepository.findByStatus(status);
    }

    @Override
    public List<Appeal> listByRoomAndStatus(Long roomId, AppealStatus status) {
        return appealRepository.findByRoomIdAndStatus(roomId, status);
    }

    @Override
    public List<Appeal> listAll() {
        return appealRepository.findAll();
    }
}
