package com.property.meter.vo;

import com.property.meter.entity.enums.AppealStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AppealVO {
    private Long id;
    private String appealNo;
    private Long billId;
    private String billNo;
    private Long roomId;
    private String roomNumber;
    private Long buildingId;
    private String buildingName;
    private String appellant;
    private String appellantPhone;
    private String appealType;
    private BigDecimal disputedUsage;
    private BigDecimal disputedAmount;
    private String appealReason;
    private String photoUrls;
    private AppealStatus status;
    private String statusName;
    private String handlerOpinion;
    private String handler;
    private LocalDateTime handleTime;
    private String remark;
    private LocalDateTime createdAt;
}
