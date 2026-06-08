package com.property.meter.dto;

import com.property.meter.entity.enums.AppealStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppealHandleDTO {

    @NotNull(message = "申诉ID不能为空")
    private Long appealId;

    @NotNull(message = "处理状态不能为空")
    private AppealStatus status;

    private String handlerOpinion;

    private String handler;
}
