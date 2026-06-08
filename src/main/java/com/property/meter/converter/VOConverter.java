package com.property.meter.converter;

import com.property.meter.entity.*;
import com.property.meter.entity.enums.AppealStatus;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.ReadingStatus;
import com.property.meter.entity.enums.SharingType;
import com.property.meter.vo.*;

import java.util.List;
import java.util.stream.Collectors;

public class VOConverter {

    private VOConverter() {}

    private static String getMeterTypeName(MeterType type) {
        if (type == null) return null;
        return type == MeterType.WATER ? "水表" : "电表";
    }

    private static String getReadingStatusName(ReadingStatus status) {
        if (status == null) return null;
        return switch (status) {
            case NORMAL -> "正常";
            case REVERSED -> "表倒走";
            case MISSING -> "漏抄";
            case ABNORMAL -> "异常";
            case VACANT -> "空置房";
        };
    }

    private static String getSharingTypeName(SharingType type) {
        if (type == null) return null;
        return switch (type) {
            case BY_AREA -> "按面积分摊";
            case BY_HOUSEHOLD -> "按户数分摊";
            case BY_BUILDING -> "按楼栋均摊";
            case CUSTOM -> "自定义";
        };
    }

    private static String getBillStatusName(BillStatus status) {
        if (status == null) return null;
        return switch (status) {
            case UNPAID -> "未缴费";
            case PAID -> "已缴费";
            case OVERDUE -> "已逾期";
            case APPEALING -> "申诉中";
        };
    }

    private static String getAppealStatusName(AppealStatus status) {
        if (status == null) return null;
        return switch (status) {
            case PENDING -> "待处理";
            case PROCESSING -> "处理中";
            case APPROVED -> "申诉通过";
            case REJECTED -> "申诉驳回";
        };
    }

    public static BuildingVO toBuildingVO(Building entity) {
        if (entity == null) return null;
        BuildingVO vo = new BuildingVO();
        vo.setId(entity.getId());
        vo.setBuildingCode(entity.getBuildingCode());
        vo.setBuildingName(entity.getBuildingName());
        vo.setTotalFloors(entity.getTotalFloors());
        vo.setTotalUnits(entity.getTotalUnits());
        vo.setLocation(entity.getLocation());
        vo.setDescription(entity.getDescription());
        vo.setCreatedAt(entity.getCreatedAt());
        return vo;
    }

    public static List<BuildingVO> toBuildingVOList(List<Building> list) {
        return list.stream().map(VOConverter::toBuildingVO).collect(Collectors.toList());
    }

    public static RoomVO toRoomVO(Room entity) {
        if (entity == null) return null;
        RoomVO vo = new RoomVO();
        vo.setId(entity.getId());
        vo.setRoomNumber(entity.getRoomNumber());
        vo.setFloor(entity.getFloor());
        vo.setUnit(entity.getUnit());
        vo.setOwnerName(entity.getOwnerName());
        vo.setOwnerPhone(entity.getOwnerPhone());
        vo.setSharingArea(entity.getSharingArea());
        vo.setHouseholdCount(entity.getHouseholdCount());
        vo.setIsVacant(entity.getIsVacant());
        vo.setCurrentTenantId(entity.getCurrentTenantId());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getBuilding() != null) {
            vo.setBuildingId(entity.getBuilding().getId());
            vo.setBuildingCode(entity.getBuilding().getBuildingCode());
            vo.setBuildingName(entity.getBuilding().getBuildingName());
        }
        return vo;
    }

    public static List<RoomVO> toRoomVOList(List<Room> list) {
        return list.stream().map(VOConverter::toRoomVO).collect(Collectors.toList());
    }

    public static MeterVO toMeterVO(Meter entity) {
        if (entity == null) return null;
        MeterVO vo = new MeterVO();
        vo.setId(entity.getId());
        vo.setMeterCode(entity.getMeterCode());
        vo.setMeterType(entity.getMeterType());
        vo.setMeterTypeName(getMeterTypeName(entity.getMeterType()));
        vo.setMeterBrand(entity.getMeterBrand());
        vo.setMeterModel(entity.getMeterModel());
        vo.setMultiplier(entity.getMultiplier());
        vo.setLastReading(entity.getLastReading());
        vo.setInstallLocation(entity.getInstallLocation());
        vo.setIsActive(entity.getIsActive());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getRoom() != null) {
            vo.setRoomId(entity.getRoom().getId());
            vo.setRoomNumber(entity.getRoom().getRoomNumber());
            if (entity.getRoom().getBuilding() != null) {
                vo.setBuildingId(entity.getRoom().getBuilding().getId());
                vo.setBuildingName(entity.getRoom().getBuilding().getBuildingName());
            }
        }
        return vo;
    }

    public static List<MeterVO> toMeterVOList(List<Meter> list) {
        return list.stream().map(VOConverter::toMeterVO).collect(Collectors.toList());
    }

    public static MeterReadingVO toMeterReadingVO(MeterReading entity) {
        if (entity == null) return null;
        MeterReadingVO vo = new MeterReadingVO();
        vo.setId(entity.getId());
        vo.setPeriod(entity.getPeriod());
        vo.setReadingDate(entity.getReadingDate());
        vo.setPreviousReading(entity.getPreviousReading());
        vo.setCurrentReading(entity.getCurrentReading());
        vo.setMultiplier(entity.getMultiplier());
        vo.setActualUsage(entity.getActualUsage());
        vo.setPhotoUrl(entity.getPhotoUrl());
        vo.setStatus(entity.getStatus());
        vo.setStatusName(getReadingStatusName(entity.getStatus()));
        vo.setMeterReader(entity.getMeterReader());
        vo.setReadTime(entity.getReadTime());
        vo.setIsVacant(entity.getIsVacant());
        vo.setHasTenantChange(entity.getHasTenantChange());
        vo.setTenantChangeRemark(entity.getTenantChangeRemark());
        vo.setIsAbnormal(entity.getIsAbnormal());
        vo.setAbnormalReason(entity.getAbnormalReason());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getMeter() != null) {
            vo.setMeterId(entity.getMeter().getId());
            vo.setMeterCode(entity.getMeter().getMeterCode());
            vo.setMeterTypeName(getMeterTypeName(entity.getMeter().getMeterType()));
            if (entity.getMeter().getRoom() != null) {
                vo.setRoomId(entity.getMeter().getRoom().getId());
                vo.setRoomNumber(entity.getMeter().getRoom().getRoomNumber());
                if (entity.getMeter().getRoom().getBuilding() != null) {
                    vo.setBuildingId(entity.getMeter().getRoom().getBuilding().getId());
                    vo.setBuildingName(entity.getMeter().getRoom().getBuilding().getBuildingName());
                }
            }
        }
        return vo;
    }

    public static List<MeterReadingVO> toMeterReadingVOList(List<MeterReading> list) {
        return list.stream().map(VOConverter::toMeterReadingVO).collect(Collectors.toList());
    }

    public static PublicMeterVO toPublicMeterVO(PublicMeter entity) {
        if (entity == null) return null;
        PublicMeterVO vo = new PublicMeterVO();
        vo.setId(entity.getId());
        vo.setMeterCode(entity.getMeterCode());
        vo.setMeterName(entity.getMeterName());
        vo.setMeterType(entity.getMeterType());
        vo.setMeterTypeName(getMeterTypeName(entity.getMeterType()));
        vo.setMeterBrand(entity.getMeterBrand());
        vo.setMeterModel(entity.getMeterModel());
        vo.setMultiplier(entity.getMultiplier());
        vo.setLastReading(entity.getLastReading());
        vo.setInstallLocation(entity.getInstallLocation());
        vo.setIsActive(entity.getIsActive());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getBuilding() != null) {
            vo.setBuildingId(entity.getBuilding().getId());
            vo.setBuildingName(entity.getBuilding().getBuildingName());
        }
        return vo;
    }

    public static List<PublicMeterVO> toPublicMeterVOList(List<PublicMeter> list) {
        return list.stream().map(VOConverter::toPublicMeterVO).collect(Collectors.toList());
    }

    public static PublicMeterReadingVO toPublicMeterReadingVO(PublicMeterReading entity) {
        if (entity == null) return null;
        PublicMeterReadingVO vo = new PublicMeterReadingVO();
        vo.setId(entity.getId());
        vo.setPeriod(entity.getPeriod());
        vo.setReadingDate(entity.getReadingDate());
        vo.setPreviousReading(entity.getPreviousReading());
        vo.setCurrentReading(entity.getCurrentReading());
        vo.setMultiplier(entity.getMultiplier());
        vo.setActualUsage(entity.getActualUsage());
        vo.setPhotoUrl(entity.getPhotoUrl());
        vo.setStatus(entity.getStatus());
        vo.setStatusName(getReadingStatusName(entity.getStatus()));
        vo.setMeterReader(entity.getMeterReader());
        vo.setReadTime(entity.getReadTime());
        vo.setIsAbnormal(entity.getIsAbnormal());
        vo.setAbnormalReason(entity.getAbnormalReason());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getPublicMeter() != null) {
            vo.setPublicMeterId(entity.getPublicMeter().getId());
            vo.setMeterCode(entity.getPublicMeter().getMeterCode());
            vo.setMeterName(entity.getPublicMeter().getMeterName());
            vo.setMeterTypeName(getMeterTypeName(entity.getPublicMeter().getMeterType()));
            if (entity.getPublicMeter().getBuilding() != null) {
                vo.setBuildingId(entity.getPublicMeter().getBuilding().getId());
                vo.setBuildingName(entity.getPublicMeter().getBuilding().getBuildingName());
            }
        }
        return vo;
    }

    public static List<PublicMeterReadingVO> toPublicMeterReadingVOList(List<PublicMeterReading> list) {
        return list.stream().map(VOConverter::toPublicMeterReadingVO).collect(Collectors.toList());
    }

    public static SharingRuleVO toSharingRuleVO(SharingRule entity) {
        if (entity == null) return null;
        SharingRuleVO vo = new SharingRuleVO();
        vo.setId(entity.getId());
        vo.setRuleName(entity.getRuleName());
        vo.setMeterType(entity.getMeterType());
        vo.setMeterTypeName(getMeterTypeName(entity.getMeterType()));
        vo.setSharingType(entity.getSharingType());
        vo.setSharingTypeName(getSharingTypeName(entity.getSharingType()));
        vo.setUnitPrice(entity.getUnitPrice());
        vo.setFixedAmount(entity.getFixedAmount());
        vo.setIsActive(entity.getIsActive());
        vo.setCustomFormula(entity.getCustomFormula());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getBuilding() != null) {
            vo.setBuildingId(entity.getBuilding().getId());
            vo.setBuildingName(entity.getBuilding().getBuildingName());
        }
        if (entity.getPublicMeter() != null) {
            vo.setPublicMeterId(entity.getPublicMeter().getId());
            vo.setPublicMeterCode(entity.getPublicMeter().getMeterCode());
            vo.setPublicMeterName(entity.getPublicMeter().getMeterName());
        }
        return vo;
    }

    public static List<SharingRuleVO> toSharingRuleVOList(List<SharingRule> list) {
        return list.stream().map(VOConverter::toSharingRuleVO).collect(Collectors.toList());
    }

    public static BillVO toBillVO(Bill entity) {
        if (entity == null) return null;
        BillVO vo = new BillVO();
        vo.setId(entity.getId());
        vo.setBillNo(entity.getBillNo());
        vo.setPeriod(entity.getPeriod());
        vo.setMeterType(entity.getMeterType());
        vo.setMeterTypeName(getMeterTypeName(entity.getMeterType()));
        vo.setPersonalUsage(entity.getPersonalUsage());
        vo.setPersonalAmount(entity.getPersonalAmount());
        vo.setSharedUsage(entity.getSharedUsage());
        vo.setSharedAmount(entity.getSharedAmount());
        vo.setTotalUsage(entity.getTotalUsage());
        vo.setTotalAmount(entity.getTotalAmount());
        vo.setUnitPrice(entity.getUnitPrice());
        vo.setStatus(entity.getStatus());
        vo.setStatusName(getBillStatusName(entity.getStatus()));
        vo.setDueDate(entity.getDueDate());
        vo.setPaidTime(entity.getPaidTime());
        vo.setPayer(entity.getPayer());
        vo.setPaymentMethod(entity.getPaymentMethod());
        vo.setPaymentRef(entity.getPaymentRef());
        vo.setSharingDetail(entity.getSharingDetail());
        vo.setIsLocked(entity.getIsLocked());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getRoom() != null) {
            vo.setRoomId(entity.getRoom().getId());
            vo.setRoomNumber(entity.getRoom().getRoomNumber());
            vo.setOwnerName(entity.getRoom().getOwnerName());
            if (entity.getRoom().getBuilding() != null) {
                vo.setBuildingId(entity.getRoom().getBuilding().getId());
                vo.setBuildingName(entity.getRoom().getBuilding().getBuildingName());
            }
        }
        return vo;
    }

    public static List<BillVO> toBillVOList(List<Bill> list) {
        return list.stream().map(VOConverter::toBillVO).collect(Collectors.toList());
    }

    public static AppealVO toAppealVO(Appeal entity) {
        if (entity == null) return null;
        AppealVO vo = new AppealVO();
        vo.setId(entity.getId());
        vo.setAppealNo(entity.getAppealNo());
        vo.setAppellant(entity.getAppellant());
        vo.setAppellantPhone(entity.getAppellantPhone());
        vo.setAppealType(entity.getAppealType());
        vo.setDisputedUsage(entity.getDisputedUsage());
        vo.setDisputedAmount(entity.getDisputedAmount());
        vo.setAppealReason(entity.getAppealReason());
        vo.setPhotoUrls(entity.getPhotoUrls());
        vo.setStatus(entity.getStatus());
        vo.setStatusName(getAppealStatusName(entity.getStatus()));
        vo.setHandlerOpinion(entity.getHandlerOpinion());
        vo.setHandler(entity.getHandler());
        vo.setHandleTime(entity.getHandleTime());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getBill() != null) {
            vo.setBillId(entity.getBill().getId());
            vo.setBillNo(entity.getBill().getBillNo());
        }
        if (entity.getRoom() != null) {
            vo.setRoomId(entity.getRoom().getId());
            vo.setRoomNumber(entity.getRoom().getRoomNumber());
            if (entity.getRoom().getBuilding() != null) {
                vo.setBuildingId(entity.getRoom().getBuilding().getId());
                vo.setBuildingName(entity.getRoom().getBuilding().getBuildingName());
            }
        }
        return vo;
    }

    public static List<AppealVO> toAppealVOList(List<Appeal> list) {
        return list.stream().map(VOConverter::toAppealVO).collect(Collectors.toList());
    }

    public static TenantVO toTenantVO(Tenant entity) {
        if (entity == null) return null;
        TenantVO vo = new TenantVO();
        vo.setId(entity.getId());
        vo.setTenantCode(entity.getTenantCode());
        vo.setName(entity.getName());
        vo.setPhone(entity.getPhone());
        vo.setIdCard(entity.getIdCard());
        vo.setMoveInDate(entity.getMoveInDate());
        vo.setMoveOutDate(entity.getMoveOutDate());
        vo.setIsActive(entity.getIsActive());
        vo.setRemark(entity.getRemark());
        vo.setCreatedAt(entity.getCreatedAt());
        if (entity.getRoom() != null) {
            vo.setRoomId(entity.getRoom().getId());
            vo.setRoomNumber(entity.getRoom().getRoomNumber());
            if (entity.getRoom().getBuilding() != null) {
                vo.setBuildingId(entity.getRoom().getBuilding().getId());
                vo.setBuildingName(entity.getRoom().getBuilding().getBuildingName());
            }
        }
        return vo;
    }

    public static List<TenantVO> toTenantVOList(List<Tenant> list) {
        return list.stream().map(VOConverter::toTenantVO).collect(Collectors.toList());
    }
}
