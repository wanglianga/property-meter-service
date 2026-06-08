package com.property.meter.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomVO {
    private Long id;
    private Long buildingId;
    private String buildingCode;
    private String buildingName;
    private String roomNumber;
    private Integer floor;
    private String unit;
    private String ownerName;
    private String ownerPhone;
    private BigDecimal sharingArea;
    private Integer householdCount;
    private Boolean isVacant;
    private String currentTenantId;
    private String remark;
    private LocalDateTime createdAt;
}
