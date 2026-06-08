package com.property.meter.vo;

import com.property.meter.entity.enums.ReadingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PublicMeterReadingVO {
    private Long id;
    private Long publicMeterId;
    private String meterCode;
    private String meterName;
    private String meterTypeName;
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
    private Boolean isAbnormal;
    private String abnormalReason;
    private String remark;
    private LocalDateTime createdAt;
}
