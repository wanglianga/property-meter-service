package com.property.meter.entity;

import com.property.meter.entity.enums.ReadingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "meter_reading")
public class MeterReading extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(nullable = false)
    private String period;

    private LocalDate readingDate;

    private BigDecimal previousReading;

    private BigDecimal currentReading;

    private BigDecimal multiplier;

    private BigDecimal actualUsage;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private ReadingStatus status;

    private String meterReader;

    private LocalDateTime readTime;

    private Boolean isVacant;

    private Boolean hasTenantChange;

    private String tenantChangeRemark;

    private Boolean isAbnormal;

    private String abnormalReason;

    private String remark;
}
