package com.property.meter.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TenantVO {
    private Long id;
    private String tenantCode;
    private Long roomId;
    private String roomNumber;
    private Long buildingId;
    private String buildingName;
    private String name;
    private String phone;
    private String idCard;
    private LocalDate moveInDate;
    private LocalDate moveOutDate;
    private Boolean isActive;
    private String remark;
    private LocalDateTime createdAt;
}
