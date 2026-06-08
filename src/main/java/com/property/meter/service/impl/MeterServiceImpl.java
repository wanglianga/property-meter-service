package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.MeterDTO;
import com.property.meter.entity.Meter;
import com.property.meter.entity.Room;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.repository.MeterRepository;
import com.property.meter.service.MeterService;
import com.property.meter.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;
    private final RoomService roomService;

    @Override
    @Transactional
    public Meter create(MeterDTO dto) {
        Room room = roomService.getById(dto.getRoomId());
        meterRepository.findByMeterCode(dto.getMeterCode())
                .ifPresent(m -> {
                    throw new BusinessException("表编号已存在: " + dto.getMeterCode());
                });
        Meter meter = new Meter();
        meter.setRoom(room);
        meter.setMeterCode(dto.getMeterCode());
        meter.setMeterType(dto.getMeterType());
        meter.setMeterBrand(dto.getMeterBrand());
        meter.setMeterModel(dto.getMeterModel());
        meter.setMultiplier(dto.getMultiplier() != null ? dto.getMultiplier() : BigDecimal.ONE);
        meter.setLastReading(dto.getLastReading() != null ? dto.getLastReading() : BigDecimal.ZERO);
        meter.setInstallLocation(dto.getInstallLocation());
        meter.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        meter.setRemark(dto.getRemark());
        return meterRepository.save(meter);
    }

    @Override
    @Transactional
    public Meter update(Long id, MeterDTO dto) {
        Meter meter = getById(id);
        if (!meter.getMeterCode().equals(dto.getMeterCode())) {
            meterRepository.findByMeterCode(dto.getMeterCode())
                    .ifPresent(m -> {
                        throw new BusinessException("表编号已存在: " + dto.getMeterCode());
                    });
        }
        if (!meter.getRoom().getId().equals(dto.getRoomId())) {
            Room room = roomService.getById(dto.getRoomId());
            meter.setRoom(room);
        }
        meter.setMeterCode(dto.getMeterCode());
        meter.setMeterType(dto.getMeterType());
        meter.setMeterBrand(dto.getMeterBrand());
        meter.setMeterModel(dto.getMeterModel());
        meter.setMultiplier(dto.getMultiplier() != null ? dto.getMultiplier() : BigDecimal.ONE);
        if (dto.getLastReading() != null) {
            meter.setLastReading(dto.getLastReading());
        }
        meter.setInstallLocation(dto.getInstallLocation());
        meter.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        meter.setRemark(dto.getRemark());
        return meterRepository.save(meter);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Meter meter = getById(id);
        meterRepository.delete(meter);
    }

    @Override
    public Meter getById(Long id) {
        return meterRepository.findById(id)
                .orElseThrow(() -> new BusinessException("表不存在, id: " + id));
    }

    @Override
    public Meter getByCode(String code) {
        return meterRepository.findByMeterCode(code)
                .orElseThrow(() -> new BusinessException("表不存在, code: " + code));
    }

    @Override
    public List<Meter> listByRoom(Long roomId) {
        return meterRepository.findByRoomId(roomId);
    }

    @Override
    public List<Meter> listByRoomAndType(Long roomId, MeterType type) {
        return meterRepository.findByRoomIdAndMeterType(roomId, type);
    }

    @Override
    public List<Meter> listAllActive() {
        return meterRepository.findByIsActiveTrue();
    }
}
