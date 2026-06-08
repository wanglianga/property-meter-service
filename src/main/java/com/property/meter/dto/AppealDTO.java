package com.property.meter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppealDTO {

    @NotNull(message = "账单ID不能为空")
    private Long billId;

    private String appellant;

    private String appellantPhone;

    private String appealType;

    private BigDecimal disputedUsage;

    private BigDecimal disputedAmount;

    @NotBlank(message = "申诉原因不能为空")
    private String appealReason;

    private String photoUrls;

    private String remark;
}
