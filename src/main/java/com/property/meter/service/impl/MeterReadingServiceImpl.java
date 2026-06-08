package com.property.meter.service.impl;

import com.property.meter.common.BusinessException;
import com.property.meter.dto.MeterReadingDTO;
import com.property.meter.entity.Meter;
import com.property.meter.entity.MeterReading;
import com.property.meter.entity.Room;
import com.property.meter.entity.enums.ReadingStatus;
import com.property.meter.repository.MeterReadingRepository;
import com.property.meter.service.MeterReadingService;
import com.property.meter.service.MeterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;
    private final MeterService meterService;

    private static final BigDecimal ABNORMAL_THRESHOLD = new BigDecimal("2.0");

    @Override
    @Transactional
    public MeterReading create(MeterReadingDTO dto) {
        Meter meter = meterService.getById(dto.getMeterId());
        Room room = meter.getRoom();

        meterReadingRepository.findByMeterIdAndPeriod(dto.getMeterId(), dto.getPeriod())
                .ifPresent(r -> {
                    throw new BusinessException("该表本期已抄录: meterId=" + dto.getMeterId() + ", period=" + dto.getPeriod());
                });

        BigDecimal previousReading = dto.getPreviousReading();
        if (previousReading == null) {
            previousReading = meter.getLastReading();
        }

        BigDecimal currentReading = dto.getCurrentReading();
        BigDecimal multiplier = dto.getMultiplier() != null ? dto.getMultiplier() : meter.getMultiplier();

        MeterReading reading = new MeterReading();
        reading.setMeter(meter);
        reading.setPeriod(dto.getPeriod());
        reading.setReadingDate(LocalDate.now());
        reading.setPreviousReading(previousReading);
        reading.setCurrentReading(currentReading);
        reading.setMultiplier(multiplier);
        reading.setPhotoUrl(dto.getPhotoUrl());
        reading.setMeterReader(dto.getMeterReader());
        reading.setReadTime(LocalDateTime.now());
        reading.setIsVacant(dto.getIsVacant() != null ? dto.getIsVacant() : Boolean.TRUE.equals(room.getIsVacant()));
        reading.setHasTenantChange(dto.getHasTenantChange() != null ? dto.getHasTenantChange() : false);
        reading.setTenantChangeRemark(dto.getTenantChangeRemark());
        reading.setRemark(dto.getRemark());

        validateAndSetStatus(reading, meter, previousReading, currentReading);

        BigDecimal usage = currentReading.subtract(previousReading).max(BigDecimal.ZERO).multiply(multiplier);
        reading.setActualUsage(usage);

        MeterReading saved = meterReadingRepository.save(reading);

        meter.setLastReading(currentReading);

        return saved;
    }

    @Override
    @Transactional
    public MeterReading update(Long id, MeterReadingDTO dto) {
        MeterReading reading = getById(id);
        Meter meter = reading.getMeter();
        Room room = meter.getRoom();

        BigDecimal previousReading = dto.getPreviousReading() != null ? dto.getPreviousReading() : reading.getPreviousReading();
        BigDecimal currentReading = dto.getCurrentReading();
        BigDecimal multiplier = dto.getMultiplier() != null ? dto.getMultiplier() : reading.getMultiplier();

        reading.setPeriod(dto.getPeriod());
        reading.setPreviousReading(previousReading);
        reading.setCurrentReading(currentReading);
        reading.setMultiplier(multiplier);
        reading.setPhotoUrl(dto.getPhotoUrl());
        reading.setMeterReader(dto.getMeterReader());
        reading.setIsVacant(dto.getIsVacant() != null ? dto.getIsVacant() : Boolean.TRUE.equals(room.getIsVacant()));
        reading.setHasTenantChange(dto.getHasTenantChange() != null ? dto.getHasTenantChange() : reading.getHasTenantChange());
        reading.setTenantChangeRemark(dto.getTenantChangeRemark());
        reading.setRemark(dto.getRemark());

        validateAndSetStatus(reading, meter, previousReading, currentReading);

        BigDecimal usage = currentReading.subtract(previousReading).max(BigDecimal.ZERO).multiply(multiplier);
        reading.setActualUsage(usage);

        return meterReadingRepository.save(reading);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MeterReading reading = getById(id);
        meterReadingRepository.delete(reading);
    }

    @Override
    public MeterReading getById(Long id) {
        return meterReadingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("抄表记录不存在, id: " + id));
    }

    @Override
    public List<MeterReading> listByMeter(Long meterId) {
        return meterReadingRepository.findByMeterId(meterId);
    }

    @Override
    public List<MeterReading> listByPeriod(String period) {
        return meterReadingRepository.findByPeriod(period);
    }

    @Override
    public List<MeterReading> listByRoomAndPeriod(Long roomId, String period) {
        return meterReadingRepository.findByRoomIdAndPeriod(roomId, period);
    }

    @Override
    public MeterReading getByMeterAndPeriod(Long meterId, String period) {
        return meterReadingRepository.findByMeterIdAndPeriod(meterId, period)
                .orElseThrow(() -> new BusinessException("抄表记录不存在, meterId=" + meterId + ", period=" + period));
    }

    private void validateAndSetStatus(MeterReading reading, Meter meter, BigDecimal previous, BigDecimal current) {
        if (Boolean.TRUE.equals(reading.getIsVacant())) {
            reading.setStatus(ReadingStatus.VACANT);
            log.info("空置房抄表: meterCode={}, period={}", meter.getMeterCode(), reading.getPeriod());
            return;
        }

        if (current.compareTo(previous) < 0) {
            reading.setStatus(ReadingStatus.REVERSED);
            reading.setIsAbnormal(true);
            reading.setAbnormalReason("表倒走: 本期读数(" + current + ") < 上期读数(" + previous + ")");
            log.warn("表倒走检测: meterCode={}, previous={}, current={}", meter.getMeterCode(), previous, current);
            return;
        }

        BigDecimal usage = current.subtract(previous);
        MeterReading lastReading = meterReadingRepository.findFirstByMeterIdOrderByReadingDateDesc(meter.getId()).orElse(null);
        if (lastReading != null && lastReading.getActualUsage() != null && lastReading.getActualUsage().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = usage.divide(lastReading.getActualUsage(), 4, BigDecimal.ROUND_HALF_UP);
            if (ratio.compareTo(ABNORMAL_THRESHOLD) > 0) {
                reading.setStatus(ReadingStatus.ABNORMAL);
                reading.setIsAbnormal(true);
                reading.setAbnormalReason("用量异常: 本期用量(" + usage + ")是上期(" + lastReading.getActualUsage() + ")的" + ratio + "倍");
                log.warn("异常用量检测: meterCode={}, ratio={}", meter.getMeterCode(), ratio);
                return;
            }
        }

        reading.setStatus(ReadingStatus.NORMAL);
        reading.setIsAbnormal(false);
    }
}
