package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.BuildingDTO;
import com.property.meter.entity.Building;
import com.property.meter.repository.BuildingRepository;
import com.property.meter.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    @Override
    @Transactional
    public Building create(BuildingDTO dto) {
        buildingRepository.findByBuildingCode(dto.getBuildingCode())
                .ifPresent(b -> {
                    throw new BusinessException("楼栋编号已存在: " + dto.getBuildingCode());
                });
        Building building = new Building();
        building.setBuildingCode(dto.getBuildingCode());
        building.setBuildingName(dto.getBuildingName());
        building.setTotalFloors(dto.getTotalFloors());
        building.setTotalUnits(dto.getTotalUnits());
        building.setLocation(dto.getLocation());
        building.setDescription(dto.getDescription());
        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public Building update(Long id, BuildingDTO dto) {
        Building building = getById(id);
        if (!building.getBuildingCode().equals(dto.getBuildingCode())) {
            buildingRepository.findByBuildingCode(dto.getBuildingCode())
                    .ifPresent(b -> {
                        throw new BusinessException("楼栋编号已存在: " + dto.getBuildingCode());
                    });
        }
        building.setBuildingCode(dto.getBuildingCode());
        building.setBuildingName(dto.getBuildingName());
        building.setTotalFloors(dto.getTotalFloors());
        building.setTotalUnits(dto.getTotalUnits());
        building.setLocation(dto.getLocation());
        building.setDescription(dto.getDescription());
        return buildingRepository.save(building);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Building building = getById(id);
        buildingRepository.delete(building);
    }

    @Override
    public Building getById(Long id) {
        return buildingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("楼栋不存在, id: " + id));
    }

    @Override
    public Building getByCode(String code) {
        return buildingRepository.findByBuildingCode(code)
                .orElseThrow(() -> new BusinessException("楼栋不存在, code: " + code));
    }

    @Override
    public List<Building> listAll() {
        return buildingRepository.findAll();
    }
}
