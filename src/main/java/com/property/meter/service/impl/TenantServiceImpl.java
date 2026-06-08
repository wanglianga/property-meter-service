package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.TenantDTO;
import com.property.meter.entity.Room;
import com.property.meter.entity.Tenant;
import com.property.meter.repository.TenantRepository;
import com.property.meter.service.RoomService;
import com.property.meter.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final RoomService roomService;

    @Override
    @Transactional
    public Tenant create(TenantDTO dto) {
        Room room = roomService.getById(dto.getRoomId());

        tenantRepository.findByTenantCode(dto.getTenantCode())
                .ifPresent(t -> {
                    throw new BusinessException("租户编号已存在: " + dto.getTenantCode());
                });

        if (Boolean.TRUE.equals(dto.getIsActive())) {
            List<Tenant> activeTenants = tenantRepository.findByRoomIdAndIsActiveTrue(dto.getRoomId());
            for (Tenant t : activeTenants) {
                t.setIsActive(false);
                tenantRepository.save(t);
            }
        }

        Tenant tenant = new Tenant();
        tenant.setTenantCode(dto.getTenantCode());
        tenant.setRoom(room);
        tenant.setName(dto.getName());
        tenant.setPhone(dto.getPhone());
        tenant.setIdCard(dto.getIdCard());
        tenant.setMoveInDate(dto.getMoveInDate());
        tenant.setMoveOutDate(dto.getMoveOutDate());
        tenant.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        tenant.setRemark(dto.getRemark());

        Tenant saved = tenantRepository.save(tenant);

        if (Boolean.TRUE.equals(dto.getIsActive())) {
            room.setCurrentTenantId(saved.getTenantCode());
            log.info("租户换约: roomId={}, newTenant={}", room.getId(), dto.getTenantCode());
        }

        return saved;
    }

    @Override
    @Transactional
    public Tenant update(Long id, TenantDTO dto) {
        Tenant tenant = getById(id);
        if (!tenant.getTenantCode().equals(dto.getTenantCode())) {
            tenantRepository.findByTenantCode(dto.getTenantCode())
                    .ifPresent(t -> {
                        throw new BusinessException("租户编号已存在: " + dto.getTenantCode());
                    });
        }
        if (!tenant.getRoom().getId().equals(dto.getRoomId())) {
            Room room = roomService.getById(dto.getRoomId());
            tenant.setRoom(room);
        }
        tenant.setTenantCode(dto.getTenantCode());
        tenant.setName(dto.getName());
        tenant.setPhone(dto.getPhone());
        tenant.setIdCard(dto.getIdCard());
        tenant.setMoveInDate(dto.getMoveInDate());
        tenant.setMoveOutDate(dto.getMoveOutDate());
        tenant.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : tenant.getIsActive());
        tenant.setRemark(dto.getRemark());
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tenant tenant = getById(id);
        tenantRepository.delete(tenant);
    }

    @Override
    public Tenant getById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new BusinessException("租户不存在, id: " + id));
    }

    @Override
    public Tenant getByCode(String code) {
        return tenantRepository.findByTenantCode(code)
                .orElseThrow(() -> new BusinessException("租户不存在, code: " + code));
    }

    @Override
    public List<Tenant> listByRoom(Long roomId) {
        return tenantRepository.findByRoomId(roomId);
    }

    @Override
    public List<Tenant> listActiveByRoom(Long roomId) {
        return tenantRepository.findByRoomIdAndIsActiveTrue(roomId);
    }

    @Override
    public List<Tenant> listAllActive() {
        return tenantRepository.findByIsActiveTrue();
    }
}
