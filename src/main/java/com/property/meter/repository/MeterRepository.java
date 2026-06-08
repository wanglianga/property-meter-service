package com.property.meter.repository;

import com.property.meter.entity.Meter;
import com.property.meter.entity.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {

    Optional<Meter> findByMeterCode(String meterCode);

    List<Meter> findByRoomId(Long roomId);

    List<Meter> findByRoomIdAndMeterType(Long roomId, MeterType meterType);

    List<Meter> findByIsActiveTrue();
}
