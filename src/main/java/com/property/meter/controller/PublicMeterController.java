package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.PublicMeterDTO;
import com.property.meter.dto.PublicMeterReadingDTO;
import com.property.meter.entity.PublicMeter;
import com.property.meter.entity.PublicMeterReading;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.PublicMeterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public-meters")
@RequiredArgsConstructor
public class PublicMeterController {

    private final PublicMeterService publicMeterService;

    @PostMapping
    public Result<PublicMeter> createMeter(@Valid @RequestBody PublicMeterDTO dto) {
        return Result.success(publicMeterService.createMeter(dto));
    }

    @PutMapping("/{id}")
    public Result<PublicMeter> updateMeter(@PathVariable Long id, @Valid @RequestBody PublicMeterDTO dto) {
        return Result.success(publicMeterService.updateMeter(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMeter(@PathVariable Long id) {
        publicMeterService.deleteMeter(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<PublicMeter> getMeterById(@PathVariable Long id) {
        return Result.success(publicMeterService.getMeterById(id));
    }

    @GetMapping("/code/{code}")
    public Result<PublicMeter> getMeterByCode(@PathVariable String code) {
        return Result.success(publicMeterService.getMeterByCode(code));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<PublicMeter>> listMetersByBuilding(@PathVariable Long buildingId) {
        return Result.success(publicMeterService.listMetersByBuilding(buildingId));
    }

    @GetMapping("/building/{buildingId}/type/{type}")
    public Result<List<PublicMeter>> listMetersByBuildingAndType(@PathVariable Long buildingId, @PathVariable MeterType type) {
        return Result.success(publicMeterService.listMetersByBuildingAndType(buildingId, type));
    }

    @GetMapping("/active")
    public Result<List<PublicMeter>> listAllActiveMeters() {
        return Result.success(publicMeterService.listAllActiveMeters());
    }

    @PostMapping("/readings")
    public Result<PublicMeterReading> createReading(@Valid @RequestBody PublicMeterReadingDTO dto) {
        return Result.success(publicMeterService.createReading(dto));
    }

    @PutMapping("/readings/{id}")
    public Result<PublicMeterReading> updateReading(@PathVariable Long id, @Valid @RequestBody PublicMeterReadingDTO dto) {
        return Result.success(publicMeterService.updateReading(id, dto));
    }

    @DeleteMapping("/readings/{id}")
    public Result<Void> deleteReading(@PathVariable Long id) {
        publicMeterService.deleteReading(id);
        return Result.success();
    }

    @GetMapping("/readings/{id}")
    public Result<PublicMeterReading> getReadingById(@PathVariable Long id) {
        return Result.success(publicMeterService.getReadingById(id));
    }

    @GetMapping("/{publicMeterId}/readings")
    public Result<List<PublicMeterReading>> listReadingsByMeter(@PathVariable Long publicMeterId) {
        return Result.success(publicMeterService.listReadingsByMeter(publicMeterId));
    }

    @GetMapping("/readings/period/{period}")
    public Result<List<PublicMeterReading>> listReadingsByPeriod(@PathVariable String period) {
        return Result.success(publicMeterService.listReadingsByPeriod(period));
    }

    @GetMapping("/{publicMeterId}/readings/period/{period}")
    public Result<PublicMeterReading> getReadingByMeterAndPeriod(@PathVariable Long publicMeterId, @PathVariable String period) {
        return Result.success(publicMeterService.getReadingByMeterAndPeriod(publicMeterId, period));
    }
}
