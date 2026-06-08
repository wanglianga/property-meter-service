package com.property.meter.service;

import com.property.meter.dto.PublicMeterDTO;
import com.property.meter.dto.PublicMeterReadingDTO;
import com.property.meter.entity.PublicMeter;
import com.property.meter.entity.PublicMeterReading;
import com.property.meter.entity.enums.MeterType;

import java.util.List;

public interface PublicMeterService {

    PublicMeter createMeter(PublicMeterDTO dto);

    PublicMeter updateMeter(Long id, PublicMeterDTO dto);

    void deleteMeter(Long id);

    PublicMeter getMeterById(Long id);

    PublicMeter getMeterByCode(String code);

    List<PublicMeter> listMetersByBuilding(Long buildingId);

    List<PublicMeter> listMetersByBuildingAndType(Long buildingId, MeterType type);

    List<PublicMeter> listAllActiveMeters();

    PublicMeterReading createReading(PublicMeterReadingDTO dto);

    PublicMeterReading updateReading(Long id, PublicMeterReadingDTO dto);

    void deleteReading(Long id);

    PublicMeterReading getReadingById(Long id);

    List<PublicMeterReading> listReadingsByMeter(Long publicMeterId);

    List<PublicMeterReading> listReadingsByPeriod(String period);

    PublicMeterReading getReadingByMeterAndPeriod(Long publicMeterId, String period);
}
