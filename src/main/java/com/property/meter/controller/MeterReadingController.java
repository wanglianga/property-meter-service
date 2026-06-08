package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.MeterReadingDTO;
import com.property.meter.entity.MeterReading;
import com.property.meter.service.MeterReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meter-readings")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService meterReadingService;

    @PostMapping
    public Result<MeterReading> create(@Valid @RequestBody MeterReadingDTO dto) {
        return Result.success(meterReadingService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<MeterReading> update(@PathVariable Long id, @Valid @RequestBody MeterReadingDTO dto) {
        return Result.success(meterReadingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        meterReadingService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<MeterReading> getById(@PathVariable Long id) {
        return Result.success(meterReadingService.getById(id));
    }

    @GetMapping("/meter/{meterId}")
    public Result<List<MeterReading>> listByMeter(@PathVariable Long meterId) {
        return Result.success(meterReadingService.listByMeter(meterId));
    }

    @GetMapping("/period/{period}")
    public Result<List<MeterReading>> listByPeriod(@PathVariable String period) {
        return Result.success(meterReadingService.listByPeriod(period));
    }

    @GetMapping("/room/{roomId}/period/{period}")
    public Result<List<MeterReading>> listByRoomAndPeriod(@PathVariable Long roomId, @PathVariable String period) {
        return Result.success(meterReadingService.listByRoomAndPeriod(roomId, period));
    }

    @GetMapping("/meter/{meterId}/period/{period}")
    public Result<MeterReading> getByMeterAndPeriod(@PathVariable Long meterId, @PathVariable String period) {
        return Result.success(meterReadingService.getByMeterAndPeriod(meterId, period));
    }
}
