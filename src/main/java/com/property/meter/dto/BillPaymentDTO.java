package com.property.meter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillPaymentDTO {

    @NotNull(message = "账单ID不能为空")
    private Long billId;

    private String payer;

    private String paymentMethod;

    private String paymentRef;
}
