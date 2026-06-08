package com.property.meter.repository;

import com.property.meter.entity.Appeal;
import com.property.meter.entity.enums.AppealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {

    Optional<Appeal> findByAppealNo(String appealNo);

    List<Appeal> findByBillId(Long billId);

    List<Appeal> findByRoomId(Long roomId);

    List<Appeal> findByStatus(AppealStatus status);

    List<Appeal> findByRoomIdAndStatus(Long roomId, AppealStatus status);
}
