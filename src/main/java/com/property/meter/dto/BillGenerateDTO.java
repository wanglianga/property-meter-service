package com.property.meter.dto;

import com.property.meter.entity.enums.MeterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillGenerateDTO {

    @NotBlank(message = "抄表周期不能为空")
    private String period;

    private Long buildingId;

    private MeterType meterType;
}
