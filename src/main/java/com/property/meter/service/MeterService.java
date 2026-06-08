package com.property.meter.service;

import com.property.meter.dto.MeterDTO;
import com.property.meter.entity.Meter;
import com.property.meter.entity.enums.MeterType;

import java.util.List;

public interface MeterService {

    Meter create(MeterDTO dto);

    Meter update(Long id, MeterDTO dto);

    void delete(Long id);

    Meter getById(Long id);

    Meter getByCode(String code);

    List<Meter> listByRoom(Long roomId);

    List<Meter> listByRoomAndType(Long roomId, MeterType type);

    List<Meter> listAllActive();
}
