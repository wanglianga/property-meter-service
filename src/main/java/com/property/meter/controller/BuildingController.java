package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.BuildingDTO;
import com.property.meter.service.BuildingService;
import com.property.meter.vo.BuildingVO;
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
    public Result<BuildingVO> create(@Valid @RequestBody BuildingDTO dto) {
        return Result.success(VOConverter.toBuildingVO(buildingService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<BuildingVO> update(@PathVariable Long id, @Valid @RequestBody BuildingDTO dto) {
        return Result.success(VOConverter.toBuildingVO(buildingService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<BuildingVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toBuildingVO(buildingService.getById(id)));
    }

    @GetMapping("/code/{code}")
    public Result<BuildingVO> getByCode(@PathVariable String code) {
        return Result.success(VOConverter.toBuildingVO(buildingService.getByCode(code)));
    }

    @GetMapping
    public Result<List<BuildingVO>> listAll() {
        return Result.success(VOConverter.toBuildingVOList(buildingService.listAll()));
    }
}
