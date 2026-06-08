package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.BillGenerateDTO;
import com.property.meter.dto.BillPaymentDTO;
import com.property.meter.entity.Bill;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/generate")
    public Result<List<Bill>> generateBills(@Valid @RequestBody BillGenerateDTO dto) {
        return Result.success(billService.generateBills(dto));
    }

    @PostMapping("/pay")
    public Result<Bill> payBill(@Valid @RequestBody BillPaymentDTO dto) {
        return Result.success(billService.payBill(dto));
    }

    @GetMapping("/{id}")
    public Result<Bill> getById(@PathVariable Long id) {
        return Result.success(billService.getById(id));
    }

    @GetMapping("/no/{billNo}")
    public Result<Bill> getByBillNo(@PathVariable String billNo) {
        return Result.success(billService.getByBillNo(billNo));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<Bill>> listByRoom(@PathVariable Long roomId) {
        return Result.success(billService.listByRoom(roomId));
    }

    @GetMapping("/room/{roomId}/period/{period}")
    public Result<List<Bill>> listByRoomAndPeriod(@PathVariable Long roomId, @PathVariable String period) {
        return Result.success(billService.listByRoomAndPeriod(roomId, period));
    }

    @GetMapping("/period/{period}")
    public Result<List<Bill>> listByPeriod(@PathVariable String period) {
        return Result.success(billService.listByPeriod(period));
    }

    @GetMapping("/status/{status}")
    public Result<List<Bill>> listByStatus(@PathVariable BillStatus status) {
        return Result.success(billService.listByStatus(status));
    }

    @GetMapping("/room/{roomId}/unpaid")
    public Result<List<Bill>> listUnpaidByRoom(@PathVariable Long roomId) {
        return Result.success(billService.listUnpaidByRoom(roomId));
    }
}
