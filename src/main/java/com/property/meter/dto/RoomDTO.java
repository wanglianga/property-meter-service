package com.property.meter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDTO {

    @NotNull(message = "楼栋ID不能为空")
    private Long buildingId;

    @NotBlank(message = "房号不能为空")
    private String roomNumber;

    private Integer floor;

    private String unit;

    private String ownerName;

    private String ownerPhone;

    private BigDecimal sharingArea;

    private Integer householdCount;

    private Boolean isVacant;

    private String remark;
}
