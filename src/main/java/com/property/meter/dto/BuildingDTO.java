package com.property.meter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuildingDTO {

    @NotBlank(message = "楼栋编号不能为空")
    private String buildingCode;

    @NotBlank(message = "楼栋名称不能为空")
    private String buildingName;

    private Integer totalFloors;

    private Integer totalUnits;

    private String location;

    private String description;
}
