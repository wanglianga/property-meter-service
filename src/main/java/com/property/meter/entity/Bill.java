package com.property.meter.entity;

import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bill")
public class Bill extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String billNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private String period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    private BigDecimal personalUsage;

    private BigDecimal personalAmount;

    private BigDecimal sharedUsage;

    private BigDecimal sharedAmount;

    private BigDecimal totalUsage;

    private BigDecimal totalAmount;

    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;

    private LocalDate dueDate;

    private LocalDateTime paidTime;

    private String payer;

    private String paymentMethod;

    private String paymentRef;

    @Column(columnDefinition = "TEXT")
    private String sharingDetail;

    private Boolean isLocked;

    private String remark;
}
