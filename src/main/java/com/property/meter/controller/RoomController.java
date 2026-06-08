package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.dto.RoomDTO;
import com.property.meter.entity.Room;
import com.property.meter.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public Result<Room> create(@Valid @RequestBody RoomDTO dto) {
        return Result.success(roomService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Room> update(@PathVariable Long id, @Valid @RequestBody RoomDTO dto) {
        return Result.success(roomService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Room> getById(@PathVariable Long id) {
        return Result.success(roomService.getById(id));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<Room>> listByBuilding(@PathVariable Long buildingId) {
        return Result.success(roomService.listByBuilding(buildingId));
    }

    @GetMapping
    public Result<List<Room>> listAll() {
        return Result.success(roomService.listAll());
    }

    @GetMapping("/vacant")
    public Result<List<Room>> listVacant() {
        return Result.success(roomService.listVacant());
    }
}
