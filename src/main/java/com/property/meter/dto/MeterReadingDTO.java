package com.property.meter.dto;

import com.property.meter.entity.enums.MeterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MeterReadingDTO {

    @NotNull(message = "表ID不能为空")
    private Long meterId;

    @NotBlank(message = "抄表周期不能为空")
    private String period;

    @NotNull(message = "本期读数不能为空")
    private BigDecimal currentReading;

    private BigDecimal previousReading;

    private BigDecimal multiplier;

    private String photoUrl;

    private String meterReader;

    private Boolean isVacant;

    private Boolean hasTenantChange;

    private String tenantChangeRemark;

    private String remark;
}
