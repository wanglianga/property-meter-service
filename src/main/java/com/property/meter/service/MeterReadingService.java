package com.property.meter.service;

import com.property.meter.dto.MeterReadingDTO;
import com.property.meter.entity.MeterReading;

import java.util.List;

public interface MeterReadingService {

    MeterReading create(MeterReadingDTO dto);

    MeterReading update(Long id, MeterReadingDTO dto);

    void delete(Long id);

    MeterReading getById(Long id);

    List<MeterReading> listByMeter(Long meterId);

    List<MeterReading> listByPeriod(String period);

    List<MeterReading> listByRoomAndPeriod(Long roomId, String period);

    MeterReading getByMeterAndPeriod(Long meterId, String period);
}
