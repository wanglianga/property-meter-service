package com.property.meter.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuildingVO {
    private Long id;
    private String buildingCode;
    private String buildingName;
    private Integer totalFloors;
    private Integer totalUnits;
    private String location;
    private String description;
    private LocalDateTime createdAt;
}
