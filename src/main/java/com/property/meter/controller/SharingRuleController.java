package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.SharingRuleDTO;
import com.property.meter.entity.SharingRule;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.service.SharingRuleService;
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
    public Result<SharingRule> create(@Valid @RequestBody SharingRuleDTO dto) {
        return Result.success(sharingRuleService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<SharingRule> update(@PathVariable Long id, @Valid @RequestBody SharingRuleDTO dto) {
        return Result.success(sharingRuleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sharingRuleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SharingRule> getById(@PathVariable Long id) {
        return Result.success(sharingRuleService.getById(id));
    }

    @GetMapping("/type/{type}")
    public Result<List<SharingRule>> listByMeterType(@PathVariable MeterType type) {
        return Result.success(sharingRuleService.listByMeterType(type));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<SharingRule>> listByBuilding(@PathVariable Long buildingId) {
        return Result.success(sharingRuleService.listByBuilding(buildingId));
    }

    @GetMapping("/building/{buildingId}/type/{type}")
    public Result<List<SharingRule>> listByBuildingAndType(@PathVariable Long buildingId, @PathVariable MeterType type) {
        return Result.success(sharingRuleService.listByBuildingAndType(buildingId, type));
    }

    @GetMapping("/active")
    public Result<List<SharingRule>> listAllActive() {
        return Result.success(sharingRuleService.listAllActive());
    }

    @GetMapping("/public-meter/{publicMeterId}")
    public Result<List<SharingRule>> listByPublicMeter(@PathVariable Long publicMeterId) {
        return Result.success(sharingRuleService.listByPublicMeter(publicMeterId));
    }
}
