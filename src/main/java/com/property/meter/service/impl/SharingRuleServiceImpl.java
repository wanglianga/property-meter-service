package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.SharingRuleDTO;
import com.property.meter.entity.Building;
import com.property.meter.entity.PublicMeter;
import com.property.meter.entity.SharingRule;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.repository.SharingRuleRepository;
import com.property.meter.service.BuildingService;
import com.property.meter.service.PublicMeterService;
import com.property.meter.service.SharingRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharingRuleServiceImpl implements SharingRuleService {

    private final SharingRuleRepository sharingRuleRepository;
    private final BuildingService buildingService;
    private final PublicMeterService publicMeterService;

    @Override
    @Transactional
    public SharingRule create(SharingRuleDTO dto) {
        SharingRule rule = new SharingRule();
        rule.setRuleName(dto.getRuleName());
        rule.setMeterType(dto.getMeterType());
        rule.setSharingType(dto.getSharingType());
        if (dto.getBuildingId() != null) {
            Building building = buildingService.getById(dto.getBuildingId());
            rule.setBuilding(building);
        }
        if (dto.getPublicMeterId() != null) {
            PublicMeter meter = publicMeterService.getMeterById(dto.getPublicMeterId());
            rule.setPublicMeter(meter);
        }
        rule.setUnitPrice(dto.getUnitPrice());
        rule.setFixedAmount(dto.getFixedAmount());
        rule.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        rule.setCustomFormula(dto.getCustomFormula());
        rule.setRemark(dto.getRemark());
        return sharingRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public SharingRule update(Long id, SharingRuleDTO dto) {
        SharingRule rule = getById(id);
        rule.setRuleName(dto.getRuleName());
        rule.setMeterType(dto.getMeterType());
        rule.setSharingType(dto.getSharingType());
        if (dto.getBuildingId() != null && (rule.getBuilding() == null || !rule.getBuilding().getId().equals(dto.getBuildingId()))) {
            Building building = buildingService.getById(dto.getBuildingId());
            rule.setBuilding(building);
        }
        if (dto.getPublicMeterId() != null && (rule.getPublicMeter() == null || !rule.getPublicMeter().getId().equals(dto.getPublicMeterId()))) {
            PublicMeter meter = publicMeterService.getMeterById(dto.getPublicMeterId());
            rule.setPublicMeter(meter);
        }
        rule.setUnitPrice(dto.getUnitPrice());
        rule.setFixedAmount(dto.getFixedAmount());
        rule.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : rule.getIsActive());
        rule.setCustomFormula(dto.getCustomFormula());
        rule.setRemark(dto.getRemark());
        return sharingRuleRepository.save(rule);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SharingRule rule = getById(id);
        sharingRuleRepository.delete(rule);
    }

    @Override
    public SharingRule getById(Long id) {
        return sharingRuleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("公摊规则不存在, id: " + id));
    }

    @Override
    public List<SharingRule> listByMeterType(MeterType type) {
        return sharingRuleRepository.findByMeterType(type);
    }

    @Override
    public List<SharingRule> listByBuilding(Long buildingId) {
        return sharingRuleRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<SharingRule> listByBuildingAndType(Long buildingId, MeterType type) {
        return sharingRuleRepository.findByBuildingIdAndMeterType(buildingId, type);
    }

    @Override
    public List<SharingRule> listAllActive() {
        return sharingRuleRepository.findByIsActiveTrue();
    }

    @Override
    public List<SharingRule> listByPublicMeter(Long publicMeterId) {
        return sharingRuleRepository.findByPublicMeterId(publicMeterId);
    }
}
