package com.property.meter.repository;

import com.property.meter.entity.Bill;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByBillNo(String billNo);

    List<Bill> findByRoomId(Long roomId);

    List<Bill> findByRoomIdAndPeriod(Long roomId, String period);

    List<Bill> findByPeriod(String period);

    List<Bill> findByStatus(BillStatus status);

    List<Bill> findByRoomIdAndMeterType(Long roomId, MeterType meterType);

    Optional<Bill> findByRoomIdAndPeriodAndMeterType(Long roomId, String period, MeterType meterType);

    boolean existsByRoomIdAndPeriodAndMeterType(Long roomId, String period, MeterType meterType);
}
