package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.BillGenerateDTO;
import com.property.meter.dto.BillPaymentDTO;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.service.BillService;
import com.property.meter.vo.BillVO;
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
    public Result<List<BillVO>> generateBills(@Valid @RequestBody BillGenerateDTO dto) {
        return Result.success(VOConverter.toBillVOList(billService.generateBills(dto)));
    }

    @PostMapping("/pay")
    public Result<BillVO> payBill(@Valid @RequestBody BillPaymentDTO dto) {
        return Result.success(VOConverter.toBillVO(billService.payBill(dto)));
    }

    @GetMapping("/{id}")
    public Result<BillVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toBillVO(billService.getById(id)));
    }

    @GetMapping("/no/{billNo}")
    public Result<BillVO> getByBillNo(@PathVariable String billNo) {
        return Result.success(VOConverter.toBillVO(billService.getByBillNo(billNo)));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<BillVO>> listByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toBillVOList(billService.listByRoom(roomId)));
    }

    @GetMapping("/room/{roomId}/period/{period}")
    public Result<List<BillVO>> listByRoomAndPeriod(@PathVariable Long roomId, @PathVariable String period) {
        return Result.success(VOConverter.toBillVOList(billService.listByRoomAndPeriod(roomId, period)));
    }

    @GetMapping("/period/{period}")
    public Result<List<BillVO>> listByPeriod(@PathVariable String period) {
        return Result.success(VOConverter.toBillVOList(billService.listByPeriod(period)));
    }

    @GetMapping("/status/{status}")
    public Result<List<BillVO>> listByStatus(@PathVariable BillStatus status) {
        return Result.success(VOConverter.toBillVOList(billService.listByStatus(status)));
    }

    @GetMapping("/room/{roomId}/unpaid")
    public Result<List<BillVO>> listUnpaidByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toBillVOList(billService.listUnpaidByRoom(roomId)));
    }
}
