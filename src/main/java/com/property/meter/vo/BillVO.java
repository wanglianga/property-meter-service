package com.property.meter.vo;

import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillVO {
    private Long id;
    private String billNo;
    private Long roomId;
    private String roomNumber;
    private Long buildingId;
    private String buildingName;
    private String ownerName;
    private String period;
    private MeterType meterType;
    private String meterTypeName;
    private BigDecimal personalUsage;
    private BigDecimal personalAmount;
    private BigDecimal sharedUsage;
    private BigDecimal sharedAmount;
    private BigDecimal totalUsage;
    private BigDecimal totalAmount;
    private BigDecimal unitPrice;
    private BillStatus status;
    private String statusName;
    private LocalDate dueDate;
    private LocalDateTime paidTime;
    private String payer;
    private String paymentMethod;
    private String paymentRef;
    private String sharingDetail;
    private Boolean isLocked;
    private String remark;
    private LocalDateTime createdAt;
}
