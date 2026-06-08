package com.property.meter.repository;

import com.property.meter.entity.SharingRule;
import com.property.meter.entity.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharingRuleRepository extends JpaRepository<SharingRule, Long> {

    List<SharingRule> findByMeterType(MeterType meterType);

    List<SharingRule> findByBuildingId(Long buildingId);

    List<SharingRule> findByBuildingIdAndMeterType(Long buildingId, MeterType meterType);

    List<SharingRule> findByIsActiveTrue();

    List<SharingRule> findByPublicMeterId(Long publicMeterId);
}
