package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.TenantDTO;
import com.property.meter.entity.Tenant;
import com.property.meter.service.TenantService;
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
    public Result<Tenant> create(@Valid @RequestBody TenantDTO dto) {
        return Result.success(tenantService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Tenant> update(@PathVariable Long id, @Valid @RequestBody TenantDTO dto) {
        return Result.success(tenantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tenantService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Tenant> getById(@PathVariable Long id) {
        return Result.success(tenantService.getById(id));
    }

    @GetMapping("/code/{code}")
    public Result<Tenant> getByCode(@PathVariable String code) {
        return Result.success(tenantService.getByCode(code));
    }

    @GetMapping("/room/{roomId}")
    public Result<List<Tenant>> listByRoom(@PathVariable Long roomId) {
        return Result.success(tenantService.listByRoom(roomId));
    }

    @GetMapping("/room/{roomId}/active")
    public Result<List<Tenant>> listActiveByRoom(@PathVariable Long roomId) {
        return Result.success(tenantService.listActiveByRoom(roomId));
    }

    @GetMapping("/active")
    public Result<List<Tenant>> listAllActive() {
        return Result.success(tenantService.listAllActive());
    }
}
