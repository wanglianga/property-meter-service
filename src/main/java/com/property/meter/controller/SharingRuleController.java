package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.SharingRuleDTO;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.SharingRuleService;
import com.property.meter.vo.SharingRuleVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sharing-rules")
@RequiredArgsConstructor
public class SharingRuleController {

    private final SharingRuleService sharingRuleService;

    @PostMapping
    public Result<SharingRuleVO> create(@Valid @RequestBody SharingRuleDTO dto) {
        return Result.success(VOConverter.toSharingRuleVO(sharingRuleService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<SharingRuleVO> update(@PathVariable Long id, @Valid @RequestBody SharingRuleDTO dto) {
        return Result.success(VOConverter.toSharingRuleVO(sharingRuleService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sharingRuleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SharingRuleVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toSharingRuleVO(sharingRuleService.getById(id)));
    }

    @GetMapping("/type/{type}")
    public Result<List<SharingRuleVO>> listByMeterType(@PathVariable MeterType type) {
        return Result.success(VOConverter.toSharingRuleVOList(sharingRuleService.listByMeterType(type)));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<SharingRuleVO>> listByBuilding(@PathVariable Long buildingId) {
        return Result.success(VOConverter.toSharingRuleVOList(sharingRuleService.listByBuilding(buildingId)));
    }

    @GetMapping("/building/{buildingId}/type/{type}")
    public Result<List<SharingRuleVO>> listByBuildingAndType(@PathVariable Long buildingId, @PathVariable MeterType type) {
        return Result.success(VOConverter.toSharingRuleVOList(sharingRuleService.listByBuildingAndType(buildingId, type)));
    }

    @GetMapping("/active")
    public Result<List<SharingRuleVO>> listAllActive() {
        return Result.success(VOConverter.toSharingRuleVOList(sharingRuleService.listAllActive()));
    }

    @GetMapping("/public-meter/{publicMeterId}")
    public Result<List<SharingRuleVO>> listByPublicMeter(@PathVariable Long publicMeterId) {
        return Result.success(VOConverter.toSharingRuleVOList(sharingRuleService.listByPublicMeter(publicMeterId)));
    }
}
