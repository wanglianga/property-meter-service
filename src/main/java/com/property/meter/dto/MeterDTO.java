package com.property.meter.dto;

import com.property.meter.entity.enums.MeterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MeterDTO {

    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @NotBlank(message = "表编号不能为空")
    private String meterCode;

    @NotNull(message = "表类型不能为空")
    private MeterType meterType;

    private String meterBrand;

    private String meterModel;

    private BigDecimal multiplier;

    private BigDecimal lastReading;

    private String installLocation;

    private Boolean isActive;

    private String remark;
}
