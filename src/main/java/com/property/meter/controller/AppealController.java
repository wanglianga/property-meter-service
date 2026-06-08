package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.AppealDTO;
import com.property.meter.dto.AppealHandleDTO;
import com.property.meter.entity.Appeal;
import com.property.meter.entity.enums.AppealStatus;
import com.property.meter.service.AppealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appeals")
@RequiredArgsConstructor
public class AppealController {

    private final AppealService appealService;

    @PostMapping
    public Result<Appeal> create(@Valid @RequestBody AppealDTO dto) {
        return Result.success(appealService.create(dto));
    }

    @PostMapping("/handle")
    public Result<Appeal> handle(@Valid @RequestBody AppealHandleDTO dto) {
        return Result.success(appealService.handle(dto));
    }

    @GetMapping("/{id}")
    public Result<Appeal> getById(@PathVariable Long id) {
        return Result.success(appealService.getById(id));
    }

    @GetMapping("/no/{appealNo}")
    public Result<Appeal> getByAppealNo(@PathVariable String appealNo) {
        return Result.success(appealService.getByAppealNo(appealNo));
    }

    @GetMapping("/bill/{billId}")
    public Result<List<Appeal>> listByBill(@PathVariable Long billId) {
        return Result.success(appealService.listByBill(billId));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<Appeal>> listByRoom(@PathVariable Long roomId) {
        return Result.success(appealService.listByRoom(roomId));
    }

    @GetMapping("/status/{status}")
    public Result<List<Appeal>> listByStatus(@PathVariable AppealStatus status) {
        return Result.success(appealService.listByStatus(status));
    }

    @GetMapping("/room/{roomId}/status/{status}")
    public Result<List<Appeal>> listByRoomAndStatus(@PathVariable Long roomId, @PathVariable AppealStatus status) {
        return Result.success(appealService.listByRoomAndStatus(roomId, status));
    }

    @GetMapping
    public Result<List<Appeal>> listAll() {
        return Result.success(appealService.listAll());
    }
}
