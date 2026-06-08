package com.property.meter.service;

import com.property.meter.dto.BillGenerateDTO;
import com.property.meter.dto.BillPaymentDTO;
import com.property.meter.entity.Bill;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;

import java.util.List;

public interface BillService {

    List<Bill> generateBills(BillGenerateDTO dto);

    Bill getById(Long id);

    Bill getByBillNo(String billNo);

    List<Bill> listByRoom(Long roomId);

    List<Bill> listByRoomAndPeriod(Long roomId, String period);

    List<Bill> listByPeriod(String period);

    List<Bill> listByStatus(BillStatus status);

    Bill payBill(BillPaymentDTO dto);

    List<Bill> listUnpaidByRoom(Long roomId);
}
