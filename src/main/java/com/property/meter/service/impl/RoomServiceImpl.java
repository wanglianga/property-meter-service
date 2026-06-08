package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.RoomDTO;
import com.property.meter.entity.Building;
import com.property.meter.entity.Room;
import com.property.meter.repository.RoomRepository;
import com.property.meter.service.BuildingService;
import com.property.meter.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BuildingService buildingService;

    @Override
    @Transactional
    public Room create(RoomDTO dto) {
        Building building = buildingService.getById(dto.getBuildingId());
        roomRepository.findByBuildingIdAndRoomNumber(dto.getBuildingId(), dto.getRoomNumber())
                .ifPresent(r -> {
                    throw new BusinessException("房间已存在: " + building.getBuildingCode() + "-" + dto.getRoomNumber());
                });
        Room room = new Room();
        room.setBuilding(building);
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setUnit(dto.getUnit());
        room.setOwnerName(dto.getOwnerName());
        room.setOwnerPhone(dto.getOwnerPhone());
        room.setSharingArea(dto.getSharingArea());
        room.setHouseholdCount(dto.getHouseholdCount() != null ? dto.getHouseholdCount() : 1);
        room.setIsVacant(dto.getIsVacant() != null ? dto.getIsVacant() : false);
        room.setRemark(dto.getRemark());
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room update(Long id, RoomDTO dto) {
        Room room = getById(id);
        if (!room.getBuilding().getId().equals(dto.getBuildingId())
                || !room.getRoomNumber().equals(dto.getRoomNumber())) {
            roomRepository.findByBuildingIdAndRoomNumber(dto.getBuildingId(), dto.getRoomNumber())
                    .ifPresent(r -> {
                        if (!r.getId().equals(id)) {
                            throw new BusinessException("房间已存在");
                        }
                    });
            Building building = buildingService.getById(dto.getBuildingId());
            room.setBuilding(building);
        }
        room.setRoomNumber(dto.getRoomNumber());
        room.setFloor(dto.getFloor());
        room.setUnit(dto.getUnit());
        room.setOwnerName(dto.getOwnerName());
        room.setOwnerPhone(dto.getOwnerPhone());
        room.setSharingArea(dto.getSharingArea());
        room.setHouseholdCount(dto.getHouseholdCount() != null ? dto.getHouseholdCount() : 1);
        room.setIsVacant(dto.getIsVacant() != null ? dto.getIsVacant() : false);
        room.setRemark(dto.getRemark());
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Room room = getById(id);
        roomRepository.delete(room);
    }

    @Override
    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new BusinessException("房间不存在, id: " + id));
    }

    @Override
    public List<Room> listByBuilding(Long buildingId) {
        return roomRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<Room> listAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> listVacant() {
        return roomRepository.findByIsVacantTrue();
    }
}
