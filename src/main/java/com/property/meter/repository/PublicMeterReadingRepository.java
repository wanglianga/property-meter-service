package com.property.meter.repository;

import com.property.meter.entity.PublicMeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicMeterReadingRepository extends JpaRepository<PublicMeterReading, Long> {

    List<PublicMeterReading> findByPublicMeterId(Long publicMeterId);

    List<PublicMeterReading> findByPeriod(String period);

    Optional<PublicMeterReading> findByPublicMeterIdAndPeriod(Long publicMeterId, String period);

    List<PublicMeterReading> findByPublicMeterIdAndReadingDateBetween(Long publicMeterId, LocalDate start, LocalDate end);
}
