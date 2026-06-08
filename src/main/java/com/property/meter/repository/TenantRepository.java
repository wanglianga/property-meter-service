package com.property.meter.repository;

import com.property.meter.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantCode(String tenantCode);

    List<Tenant> findByRoomId(Long roomId);

    List<Tenant> findByRoomIdAndIsActiveTrue(Long roomId);

    List<Tenant> findByIsActiveTrue();
}
