package com.property.meter.repository;

import com.property.meter.entity.PublicMeter;
import com.property.meter.entity.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicMeterRepository extends JpaRepository<PublicMeter, Long> {

    Optional<PublicMeter> findByMeterCode(String meterCode);

    List<PublicMeter> findByBuildingId(Long buildingId);

    List<PublicMeter> findByBuildingIdAndMeterType(Long buildingId, MeterType meterType);

    List<PublicMeter> findByIsActiveTrue();
}
