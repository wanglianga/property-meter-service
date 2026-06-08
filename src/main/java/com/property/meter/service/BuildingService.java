package com.property.meter.service;

import com.property.meter.dto.BuildingDTO;
import com.property.meter.entity.Building;

import java.util.List;

public interface BuildingService {

    Building create(BuildingDTO dto);

    Building update(Long id, BuildingDTO dto);

    void delete(Long id);

    Building getById(Long id);

    Building getByCode(String code);

    List<Building> listAll();
}
