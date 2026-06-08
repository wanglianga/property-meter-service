package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.MeterDTO;
import com.property.meter.entity.Meter;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.MeterService;
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
    public Result<Meter> create(@Valid @RequestBody MeterDTO dto) {
        return Result.success(meterService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Meter> update(@PathVariable Long id, @Valid @RequestBody MeterDTO dto) {
        return Result.success(meterService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        meterService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Meter> getById(@PathVariable Long id) {
        return Result.success(meterService.getById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Meter> getByCode(@PathVariable String code) {
        return Result.success(meterService.getByCode(code));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<Meter>> listByRoom(@PathVariable Long roomId) {
        return Result.success(meterService.listByRoom(roomId));
    }

    @GetMapping("/room/{roomId}/type/{type}")
    public Result<List<Meter>> listByRoomAndType(@PathVariable Long roomId, @PathVariable MeterType type) {
        return Result.success(meterService.listByRoomAndType(roomId, type));
    }

    @GetMapping("/active")
    public Result<List<Meter>> listAllActive() {
        return Result.success(meterService.listAllActive());
    }
}
