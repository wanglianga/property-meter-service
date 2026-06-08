package com.property.meter.vo;

import com.property.meter.entity.enums.ReadingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MeterReadingVO {
    private Long id;
    private Long meterId;
    private String meterCode;
    private String meterTypeName;
    private Long roomId;
    private String roomNumber;
    private Long buildingId;
    private String buildingName;
    private String period;
    private LocalDate readingDate;
    private BigDecimal previousReading;
    private BigDecimal currentReading;
    private BigDecimal multiplier;
    private BigDecimal actualUsage;
    private String photoUrl;
    private ReadingStatus status;
    private String statusName;
    private String meterReader;
    private LocalDateTime readTime;
    private Boolean isVacant;
    private Boolean hasTenantChange;
    private String tenantChangeRemark;
    private Boolean isAbnormal;
    private String abnormalReason;
    private String remark;
    private LocalDateTime createdAt;
}
