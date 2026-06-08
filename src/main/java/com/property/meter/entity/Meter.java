package com.property.meter.entity;

import com.property.meter.entity.enums.MeterType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "meter")
public class Meter extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false, unique = true)
    private String meterCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    private String meterBrand;

    private String meterModel;

    private BigDecimal multiplier;

    private BigDecimal lastReading;

    private String installLocation;

    private Boolean isActive;

    private String remark;
}
