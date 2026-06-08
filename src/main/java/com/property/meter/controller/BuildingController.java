package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.BuildingDTO;
import com.property.meter.entity.Building;
import com.property.meter.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    public Result<Building> create(@Valid @RequestBody BuildingDTO dto) {
        return Result.success(buildingService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Building> update(@PathVariable Long id, @Valid @RequestBody BuildingDTO dto) {
        return Result.success(buildingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Building> getById(@PathVariable Long id) {
        return Result.success(buildingService.getById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Building> getByCode(@PathVariable String code) {
        return Result.success(buildingService.getByCode(code));
    }

    @GetMapping
    public Result<List<Building>> listAll() {
        return Result.success(buildingService.listAll());
    }
}
