package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.AppealDTO;
import com.property.meter.dto.AppealHandleDTO;
import com.property.meter.entity.enums.AppealStatus;
import com.property.meter.service.AppealService;
import com.property.meter.vo.AppealVO;
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
    public Result<AppealVO> create(@Valid @RequestBody AppealDTO dto) {
        return Result.success(VOConverter.toAppealVO(appealService.create(dto)));
    }

    @PostMapping("/handle")
    public Result<AppealVO> handle(@Valid @RequestBody AppealHandleDTO dto) {
        return Result.success(VOConverter.toAppealVO(appealService.handle(dto)));
    }

    @GetMapping("/{id}")
    public Result<AppealVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toAppealVO(appealService.getById(id)));
    }

    @GetMapping("/no/{appealNo}")
    public Result<AppealVO> getByAppealNo(@PathVariable String appealNo) {
        return Result.success(VOConverter.toAppealVO(appealService.getByAppealNo(appealNo)));
    }

    @GetMapping("/bill/{billId}")
    public Result<List<AppealVO>> listByBill(@PathVariable Long billId) {
        return Result.success(VOConverter.toAppealVOList(appealService.listByBill(billId)));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<AppealVO>> listByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toAppealVOList(appealService.listByRoom(roomId)));
    }

    @GetMapping("/status/{status}")
    public Result<List<AppealVO>> listByStatus(@PathVariable AppealStatus status) {
        return Result.success(VOConverter.toAppealVOList(appealService.listByStatus(status)));
    }

    @GetMapping("/room/{roomId}/status/{status}")
    public Result<List<AppealVO>> listByRoomAndStatus(@PathVariable Long roomId, @PathVariable AppealStatus status) {
        return Result.success(VOConverter.toAppealVOList(appealService.listByRoomAndStatus(roomId, status)));
    }

    @GetMapping
    public Result<List<AppealVO>> listAll() {
        return Result.success(VOConverter.toAppealVOList(appealService.listAll()));
    }
}
