package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.MeterReadingDTO;
import com.property.meter.service.MeterReadingService;
import com.property.meter.vo.MeterReadingVO;
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
    public Result<MeterReadingVO> create(@Valid @RequestBody MeterReadingDTO dto) {
        return Result.success(VOConverter.toMeterReadingVO(meterReadingService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<MeterReadingVO> update(@PathVariable Long id, @Valid @RequestBody MeterReadingDTO dto) {
        return Result.success(VOConverter.toMeterReadingVO(meterReadingService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        meterReadingService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<MeterReadingVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toMeterReadingVO(meterReadingService.getById(id)));
    }

    @GetMapping("/meter/{meterId}")
    public Result<List<MeterReadingVO>> listByMeter(@PathVariable Long meterId) {
        return Result.success(VOConverter.toMeterReadingVOList(meterReadingService.listByMeter(meterId)));
    }

    @GetMapping("/period/{period}")
    public Result<List<MeterReadingVO>> listByPeriod(@PathVariable String period) {
        return Result.success(VOConverter.toMeterReadingVOList(meterReadingService.listByPeriod(period)));
    }

    @GetMapping("/room/{roomId}/period/{period}")
    public Result<List<MeterReadingVO>> listByRoomAndPeriod(@PathVariable Long roomId, @PathVariable String period) {
        return Result.success(VOConverter.toMeterReadingVOList(meterReadingService.listByRoomAndPeriod(roomId, period)));
    }

    @GetMapping("/meter/{meterId}/period/{period}")
    public Result<MeterReadingVO> getByMeterAndPeriod(@PathVariable Long meterId, @PathVariable String period) {
        return Result.success(VOConverter.toMeterReadingVO(meterReadingService.getByMeterAndPeriod(meterId, period)));
    }
}
