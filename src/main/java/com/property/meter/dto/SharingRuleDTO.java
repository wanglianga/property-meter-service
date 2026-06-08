package com.property.meter.dto;

import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.SharingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SharingRuleDTO {

    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    @NotNull(message = "表类型不能为空")
    private MeterType meterType;

    @NotNull(message = "分摊类型不能为空")
    private SharingType sharingType;

    private Long buildingId;

    private Long publicMeterId;

    private BigDecimal unitPrice;

    private BigDecimal fixedAmount;

    private Boolean isActive;

    private String customFormula;

    private String remark;
}
