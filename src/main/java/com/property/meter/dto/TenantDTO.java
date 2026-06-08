package com.property.meter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TenantDTO {

    @NotBlank(message = "租户编号不能为空")
    private String tenantCode;

    @NotNull(message = "房间ID不能为空")
    private Long roomId;

    @NotBlank(message = "租户姓名不能为空")
    private String name;

    private String phone;

    private String idCard;

    private LocalDate moveInDate;

    private LocalDate moveOutDate;

    private Boolean isActive;

    private String remark;
}
