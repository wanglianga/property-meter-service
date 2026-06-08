package com.property.meter.repository;

import com.property.meter.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByBuildingId(Long buildingId);

    Optional<Room> findByBuildingIdAndRoomNumber(Long buildingId, String roomNumber);

    List<Room> findByIsVacantTrue();

    List<Room> findByCurrentTenantId(String tenantId);
}
