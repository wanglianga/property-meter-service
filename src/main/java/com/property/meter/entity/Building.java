package com.property.meter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "building")
public class Building extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String buildingCode;

    @Column(nullable = false)
    private String buildingName;

    private Integer totalFloors;

    private Integer totalUnits;

    private String location;

    private String description;
}
