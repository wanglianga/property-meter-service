package com.property.meter.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "room", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"building_id", "roomNumber"})
})
public class Room extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(nullable = false)
    private String roomNumber;

    private Integer floor;

    private String unit;

    private String ownerName;

    private String ownerPhone;

    private BigDecimal sharingArea;

    private Integer householdCount;

    private Boolean isVacant;

    private String currentTenantId;

    private String remark;
}
