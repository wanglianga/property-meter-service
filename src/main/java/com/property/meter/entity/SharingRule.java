package com.property.meter.entity;

import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.SharingType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sharing_rule")
public class SharingRule extends BaseEntity {

    @Column(nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharingType sharingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_meter_id")
    private PublicMeter publicMeter;

    private BigDecimal unitPrice;

    private BigDecimal fixedAmount;

    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String customFormula;

    private String remark;
}
