package com.property.meter.service;

import com.property.meter.dto.RoomDTO;
import com.property.meter.entity.Room;

import java.util.List;

public interface RoomService {

    Room create(RoomDTO dto);

    Room update(Long id, RoomDTO dto);

    void delete(Long id);

    Room getById(Long id);

    List<Room> listByBuilding(Long buildingId);

    List<Room> listAll();

    List<Room> listVacant();
}
