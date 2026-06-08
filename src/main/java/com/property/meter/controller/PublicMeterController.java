package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.PublicMeterDTO;
import com.property.meter.dto.PublicMeterReadingDTO;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.PublicMeterService;
import com.property.meter.vo.PublicMeterReadingVO;
import com.property.meter.vo.PublicMeterVO;
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
    public Result<PublicMeterVO> createMeter(@Valid @RequestBody PublicMeterDTO dto) {
        return Result.success(VOConverter.toPublicMeterVO(publicMeterService.createMeter(dto)));
    }

    @PutMapping("/{id}")
    public Result<PublicMeterVO> updateMeter(@PathVariable Long id, @Valid @RequestBody PublicMeterDTO dto) {
        return Result.success(VOConverter.toPublicMeterVO(publicMeterService.updateMeter(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMeter(@PathVariable Long id) {
        publicMeterService.deleteMeter(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<PublicMeterVO> getMeterById(@PathVariable Long id) {
        return Result.success(VOConverter.toPublicMeterVO(publicMeterService.getMeterById(id)));
    }

    @GetMapping("/code/{code}")
    public Result<PublicMeterVO> getMeterByCode(@PathVariable String code) {
        return Result.success(VOConverter.toPublicMeterVO(publicMeterService.getMeterByCode(code)));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<PublicMeterVO>> listMetersByBuilding(@PathVariable Long buildingId) {
        return Result.success(VOConverter.toPublicMeterVOList(publicMeterService.listMetersByBuilding(buildingId)));
    }

    @GetMapping("/building/{buildingId}/type/{type}")
    public Result<List<PublicMeterVO>> listMetersByBuildingAndType(@PathVariable Long buildingId, @PathVariable MeterType type) {
        return Result.success(VOConverter.toPublicMeterVOList(publicMeterService.listMetersByBuildingAndType(buildingId, type)));
    }

    @GetMapping("/active")
    public Result<List<PublicMeterVO>> listAllActiveMeters() {
        return Result.success(VOConverter.toPublicMeterVOList(publicMeterService.listAllActiveMeters()));
    }

    @PostMapping("/readings")
    public Result<PublicMeterReadingVO> createReading(@Valid @RequestBody PublicMeterReadingDTO dto) {
        return Result.success(VOConverter.toPublicMeterReadingVO(publicMeterService.createReading(dto)));
    }

    @PutMapping("/readings/{id}")
    public Result<PublicMeterReadingVO> updateReading(@PathVariable Long id, @Valid @RequestBody PublicMeterReadingDTO dto) {
        return Result.success(VOConverter.toPublicMeterReadingVO(publicMeterService.updateReading(id, dto)));
    }

    @DeleteMapping("/readings/{id}")
    public Result<Void> deleteReading(@PathVariable Long id) {
        publicMeterService.deleteReading(id);
        return Result.success();
    }

    @GetMapping("/readings/{id}")
    public Result<PublicMeterReadingVO> getReadingById(@PathVariable Long id) {
        return Result.success(VOConverter.toPublicMeterReadingVO(publicMeterService.getReadingById(id)));
    }

    @GetMapping("/{publicMeterId}/readings")
    public Result<List<PublicMeterReadingVO>> listReadingsByMeter(@PathVariable Long publicMeterId) {
        return Result.success(VOConverter.toPublicMeterReadingVOList(publicMeterService.listReadingsByMeter(publicMeterId)));
    }

    @GetMapping("/readings/period/{period}")
    public Result<List<PublicMeterReadingVO>> listReadingsByPeriod(@PathVariable String period) {
        return Result.success(VOConverter.toPublicMeterReadingVOList(publicMeterService.listReadingsByPeriod(period)));
    }

    @GetMapping("/{publicMeterId}/readings/period/{period}")
    public Result<PublicMeterReadingVO> getReadingByMeterAndPeriod(@PathVariable Long publicMeterId, @PathVariable String period) {
        return Result.success(VOConverter.toPublicMeterReadingVO(publicMeterService.getReadingByMeterAndPeriod(publicMeterId, period)));
    }
}
