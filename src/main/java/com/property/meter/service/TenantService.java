package com.property.meter.service;

import com.property.meter.dto.TenantDTO;
import com.property.meter.entity.Tenant;

import java.util.List;

public interface TenantService {

    Tenant create(TenantDTO dto);

    Tenant update(Long id, TenantDTO dto);

    void delete(Long id);

    Tenant getById(Long id);

    Tenant getByCode(String code);

    List<Tenant> listByRoom(Long roomId);

    List<Tenant> listActiveByRoom(Long roomId);

    List<Tenant> listAllActive();
}
