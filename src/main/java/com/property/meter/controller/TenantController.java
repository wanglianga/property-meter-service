package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.TenantDTO;
import com.property.meter.service.TenantService;
import com.property.meter.vo.TenantVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public Result<TenantVO> create(@Valid @RequestBody TenantDTO dto) {
        return Result.success(VOConverter.toTenantVO(tenantService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<TenantVO> update(@PathVariable Long id, @Valid @RequestBody TenantDTO dto) {
        return Result.success(VOConverter.toTenantVO(tenantService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TenantVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toTenantVO(tenantService.getById(id)));
    }

    @GetMapping("/code/{code}")
    public Result<TenantVO> getByCode(@PathVariable String code) {
        return Result.success(VOConverter.toTenantVO(tenantService.getByCode(code)));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<TenantVO>> listByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toTenantVOList(tenantService.listByRoom(roomId)));
    }

    @GetMapping("/room/{roomId}/active")
    public Result<List<TenantVO>> listActiveByRoom(@PathVariable Long roomId) {
        return Result.success(VOConverter.toTenantVOList(tenantService.listActiveByRoom(roomId)));
    }

    @GetMapping("/active")
    public Result<List<TenantVO>> listAllActive() {
        return Result.success(VOConverter.toTenantVOList(tenantService.listAllActive()));
    }
}
