package com.property.meter.vo;

import com.property.meter.entity.enums.MeterType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PublicMeterVO {
    private Long id;
    private Long buildingId;
    private String buildingName;
    private String meterCode;
    private String meterName;
    private MeterType meterType;
    private String meterTypeName;
    private String meterBrand;
    private String meterModel;
    private BigDecimal multiplier;
    private BigDecimal lastReading;
    private String installLocation;
    private Boolean isActive;
    private String remark;
    private LocalDateTime createdAt;
}
