package com.property.meter.vo;

import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.SharingType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SharingRuleVO {
    private Long id;
    private String ruleName;
    private MeterType meterType;
    private String meterTypeName;
    private SharingType sharingType;
    private String sharingTypeName;
    private Long buildingId;
    private String buildingName;
    private Long publicMeterId;
    private String publicMeterCode;
    private String publicMeterName;
    private BigDecimal unitPrice;
    private BigDecimal fixedAmount;
    private Boolean isActive;
    private String customFormula;
    private String remark;
    private LocalDateTime createdAt;
}
