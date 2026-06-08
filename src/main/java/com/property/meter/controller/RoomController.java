package com.property.meter.controller;

import com.property.meter.common.Result;
import com.property.meter.converter.VOConverter;
import com.property.meter.dto.RoomDTO;
import com.property.meter.service.RoomService;
import com.property.meter.vo.RoomVO;
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
    public Result<RoomVO> create(@Valid @RequestBody RoomDTO dto) {
        return Result.success(VOConverter.toRoomVO(roomService.create(dto)));
    }

    @PutMapping("/{id}")
    public Result<RoomVO> update(@PathVariable Long id, @Valid @RequestBody RoomDTO dto) {
        return Result.success(VOConverter.toRoomVO(roomService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<RoomVO> getById(@PathVariable Long id) {
        return Result.success(VOConverter.toRoomVO(roomService.getById(id)));
    }

    @GetMapping("/building/{buildingId}")
    public Result<List<RoomVO>> listByBuilding(@PathVariable Long buildingId) {
        return Result.success(VOConverter.toRoomVOList(roomService.listByBuilding(buildingId)));
    }

    @GetMapping
    public Result<List<RoomVO>> listAll() {
        return Result.success(VOConverter.toRoomVOList(roomService.listAll()));
    }

    @GetMapping("/vacant")
    public Result<List<RoomVO>> listVacant() {
        return Result.success(VOConverter.toRoomVOList(roomService.listVacant()));
    }
}
