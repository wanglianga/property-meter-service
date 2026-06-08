package com.property.meter.repository;

import com.property.meter.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    List<MeterReading> findByMeterId(Long meterId);

    List<MeterReading> findByPeriod(String period);

    Optional<MeterReading> findByMeterIdAndPeriod(Long meterId, String period);

    @Query("SELECT mr FROM MeterReading mr WHERE mr.meter.room.id = :roomId AND mr.period = :period")
    List<MeterReading> findByRoomIdAndPeriod(Long roomId, String period);

    List<MeterReading> findByMeterIdAndReadingDateBetween(Long meterId, LocalDate start, LocalDate end);

    Optional<MeterReading> findFirstByMeterIdOrderByReadingDateDesc(Long meterId);
}
