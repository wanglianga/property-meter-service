package com.property.meter.service;

import com.property.meter.dto.SharingRuleDTO;
import com.property.meter.entity.SharingRule;
import com.property.meter.entity.enums.MeterType;

import java.util.List;

public interface SharingRuleService {

    SharingRule create(SharingRuleDTO dto);

    SharingRule update(Long id, SharingRuleDTO dto);

    void delete(Long id);

    SharingRule getById(Long id);

    List<SharingRule> listByMeterType(MeterType type);

    List<SharingRule> listByBuilding(Long buildingId);

    List<SharingRule> listByBuildingAndType(Long buildingId, MeterType type);

    List<SharingRule> listAllActive();

    List<SharingRule> listByPublicMeter(Long publicMeterId);
}
