package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.MeterDTO;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.MeterService;
import com.property.meter.vo.MeterVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;

    @PostMapping
    public Result<MeterVO> create(@Valid @RequestBody MeterDTO dto) {
        return Result.success(VOConverter.toMeterVO(meterService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<MeterVO> update(@PathVariable Long id, @Valid @RequestBody MeterDTO dto) {
        return Result.success(VOConverter.toMeterVO(meterService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        meterService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<MeterVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toMeterVO(meterService.getById(id)));
    }

    @GetMapping("/code/{code}")
    public Result<MeterVO> getByCode(@PathVariable String code) {
        return Result.success(VOConverter.toMeterVO(meterService.getByCode(code)));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<MeterVO>> listByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toMeterVOList(meterService.listByRoom(roomId)));
    }

    @GetMapping("/room/{roomId}/type/{type}")
    public Result<List<MeterVO>> listByRoomAndType(@PathVariable Long roomId, @PathVariable MeterType type) {
        return Result.success(VOConverter.toMeterVOList(meterService.listByRoomAndType(roomId, type)));
    }

    @GetMapping("/active")
    public Result<List<MeterVO>> listAllActive() {
        return Result.success(VOConverter.toMeterVOList(meterService.listAllActive()));
    }
}
