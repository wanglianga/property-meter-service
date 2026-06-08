package com.property.meter.entity;

import com.property.meter.entity.enums.AppealStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "appeal")
public class Appeal extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String appealNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private String appellant;

    private String appellantPhone;

    private String appealType;

    private BigDecimal disputedUsage;

    private BigDecimal disputedAmount;

    @Column(columnDefinition = "TEXT")
    private String appealReason;

    private String photoUrls;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppealStatus status;

    @Column(columnDefinition = "TEXT")
    private String handlerOpinion;

    private String handler;

    private LocalDateTime handleTime;

    @Column(columnDefinition = "TEXT")
    private String remark;
}
