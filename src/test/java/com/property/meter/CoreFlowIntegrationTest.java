package com.property.meter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.property.meter.dto.*;
import com.property.meter.entity.enums.AppealStatus;
import com.property.meter.entity.enums.BillStatus;
import com.property.meter.entity.enums.MeterType;
import com.property.meter.entity.enums.SharingType;
import com.property.meter.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CoreFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long buildingId;
    private Long roomId;
    private Long meterId;
    private Long publicMeterId;
    private Long sharingRuleId;
    private String period = "2026-05";

    @Test
    @DisplayName("核心链路回归验证：创建水电表→房间抄表→公区抄表→账单生成→缴费→申诉")
    void testCoreFlow() throws Exception {
        testStep1_createBuilding();
        testStep2_createRoom();
        testStep3_createMeter();
        testStep4_createPublicMeter();
        testStep5_createSharingRule();
        testStep6_createMeterReading();
        testStep7_createPublicMeterReading();
        testStep8_generateBills();
        testStep9_payBill();
        testStep10_createAppeal();
    }

    void testStep1_createBuilding() throws Exception {
        BuildingDTO dto = new BuildingDTO();
        dto.setBuildingCode("B001");
        dto.setBuildingName("1号楼");
        dto.setTotalFloors(18);
        dto.setTotalUnits(72);

        String resp = mockMvc.perform(post("/api/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.buildingCode").value("B001"))
                .andExpect(jsonPath("$.data.buildingName").value("1号楼"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andExpect(jsonPath("$.data.handler").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        BuildingVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(BuildingVO.class);
        buildingId = vo.getId();
        assertNotNull(buildingId);
        System.out.println("Step 1 ✅ 创建楼栋成功: id=" + buildingId);
    }

    void testStep2_createRoom() throws Exception {
        RoomDTO dto = new RoomDTO();
        dto.setBuildingId(buildingId);
        dto.setRoomNumber("101");
        dto.setFloor(1);
        dto.setOwnerName("张三");
        dto.setOwnerPhone("13800138000");
        dto.setSharingArea(new BigDecimal("89.5"));
        dto.setHouseholdCount(3);
        dto.setIsVacant(false);

        String resp = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.roomNumber").value("101"))
                .andExpect(jsonPath("$.data.buildingId").value(buildingId))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        RoomVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(RoomVO.class);
        roomId = vo.getId();
        assertNotNull(roomId);
        System.out.println("Step 2 ✅ 创建房间成功: id=" + roomId);
    }

    void testStep3_createMeter() throws Exception {
        MeterDTO dto = new MeterDTO();
        dto.setRoomId(roomId);
        dto.setMeterCode("E-B001-101");
        dto.setMeterType(MeterType.ELECTRICITY);
        dto.setMeterBrand("国家电网");
        dto.setMultiplier(BigDecimal.ONE);
        dto.setLastReading(new BigDecimal("1000.0"));
        dto.setIsActive(true);

        String resp = mockMvc.perform(post("/api/meters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.meterCode").value("E-B001-101"))
                .andExpect(jsonPath("$.data.meterTypeName").value("电表"))
                .andExpect(jsonPath("$.data.roomId").value(roomId))
                .andExpect(jsonPath("$.data.roomNumber").value("101"))
                .andExpect(jsonPath("$.data.buildingId").value(buildingId))
                .andExpect(jsonPath("$.data.buildingName").value("1号楼"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andExpect(jsonPath("$.data.handler").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        MeterVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(MeterVO.class);
        meterId = vo.getId();
        assertNotNull(meterId);
        System.out.println("Step 3 ✅ 创建水电表成功: id=" + meterId + ", JSON无lazy proxy属性");
    }

    void testStep4_createPublicMeter() throws Exception {
        PublicMeterDTO dto = new PublicMeterDTO();
        dto.setBuildingId(buildingId);
        dto.setMeterCode("EP-B001-01");
        dto.setMeterName("1号楼公区总电表");
        dto.setMeterType(MeterType.ELECTRICITY);
        dto.setMultiplier(BigDecimal.ONE);
        dto.setLastReading(new BigDecimal("50000.0"));
        dto.setIsActive(true);

        String resp = mockMvc.perform(post("/api/public-meters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.meterCode").value("EP-B001-01"))
                .andExpect(jsonPath("$.data.buildingId").value(buildingId))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        PublicMeterVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(PublicMeterVO.class);
        publicMeterId = vo.getId();
        assertNotNull(publicMeterId);
        System.out.println("Step 4 ✅ 创建公区表成功: id=" + publicMeterId);
    }

    void testStep5_createSharingRule() throws Exception {
        SharingRuleDTO dto = new SharingRuleDTO();
        dto.setRuleName("1号楼公区电费按面积分摊");
        dto.setMeterType(MeterType.ELECTRICITY);
        dto.setSharingType(SharingType.BY_AREA);
        dto.setBuildingId(buildingId);
        dto.setPublicMeterId(publicMeterId);
        dto.setUnitPrice(new BigDecimal("0.6"));
        dto.setIsActive(true);

        String resp = mockMvc.perform(post("/api/sharing-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.ruleName").value("1号楼公区电费按面积分摊"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        SharingRuleVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(SharingRuleVO.class);
        sharingRuleId = vo.getId();
        assertNotNull(sharingRuleId);
        System.out.println("Step 5 ✅ 创建公摊规则成功: id=" + sharingRuleId);
    }

    void testStep6_createMeterReading() throws Exception {
        MeterReadingDTO dto = new MeterReadingDTO();
        dto.setMeterId(meterId);
        dto.setPeriod(period);
        dto.setCurrentReading(new BigDecimal("1150.0"));
        dto.setMeterReader("抄表员小王");
        dto.setIsVacant(false);

        String resp = mockMvc.perform(post("/api/meter-readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.period").value(period))
                .andExpect(jsonPath("$.data.actualUsage").value(150.0))
                .andExpect(jsonPath("$.data.statusName").value("正常"))
                .andExpect(jsonPath("$.data.roomNumber").value("101"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        MeterReadingVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(MeterReadingVO.class);
        assertNotNull(vo.getId());
        assertEquals(new BigDecimal("150.0"), vo.getActualUsage());
        System.out.println("Step 6 ✅ 房间抄表成功: 用量=150度, 状态=" + vo.getStatusName());
    }

    void testStep7_createPublicMeterReading() throws Exception {
        PublicMeterReadingDTO dto = new PublicMeterReadingDTO();
        dto.setPublicMeterId(publicMeterId);
        dto.setPeriod(period);
        dto.setCurrentReading(new BigDecimal("51000.0"));
        dto.setMeterReader("抄表员小王");

        String resp = mockMvc.perform(post("/api/public-meters/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.period").value(period))
                .andExpect(jsonPath("$.data.actualUsage").value(1000.0))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        PublicMeterReadingVO vo = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(PublicMeterReadingVO.class);
        assertNotNull(vo.getId());
        assertEquals(new BigDecimal("1000.0"), vo.getActualUsage());
        System.out.println("Step 7 ✅ 公区抄表成功: 用量=1000度");
    }

    void testStep8_generateBills() throws Exception {
        BillGenerateDTO dto = new BillGenerateDTO();
        dto.setPeriod(period);
        dto.setBuildingId(buildingId);
        dto.setMeterType(MeterType.ELECTRICITY);

        String resp = mockMvc.perform(post("/api/bills/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].period").value(period))
                .andExpect(jsonPath("$.data[0].meterTypeName").value("电表"))
                .andExpect(jsonPath("$.data[0].personalUsage").value(150.0))
                .andExpect(jsonPath("$.data[0].status").value("UNPAID"))
                .andExpect(jsonPath("$.data[0].statusName").value("未缴费"))
                .andExpect(jsonPath("$.data[0].roomNumber").value("101"))
                .andExpect(jsonPath("$.data[0].ownerName").value("张三"))
                .andExpect(jsonPath("$.data[0].hibernateLazyInitializer").doesNotExist())
                .andExpect(jsonPath("$.data[0].handler").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Step 8 ✅ 账单生成成功, JSON序列化无lazy proxy");
    }

    void testStep9_payBill() throws Exception {
        String listResp = mockMvc.perform(get("/api/bills/room/{roomId}/period/{period}", roomId, period))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn().getResponse().getContentAsString();

        com.fasterxml.jackson.databind.JsonNode billsNode = objectMapper.readTree(listResp).get("data");
        assertTrue(billsNode.isArray() && billsNode.size() > 0, "应有生成的账单");
        Long billId = billsNode.get(0).get("id").asLong();

        BillPaymentDTO dto = new BillPaymentDTO();
        dto.setBillId(billId);
        dto.setPayer("张三");
        dto.setPaymentMethod("微信支付");
        dto.setPaymentRef("WX20260608001");

        String resp = mockMvc.perform(post("/api/bills/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(billId))
                .andExpect(jsonPath("$.data.status").value("PAID"))
                .andExpect(jsonPath("$.data.statusName").value("已缴费"))
                .andExpect(jsonPath("$.data.isLocked").value(true))
                .andExpect(jsonPath("$.data.payer").value("张三"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        BillVO bill = objectMapper.readTree(resp).get("data").traverse(objectMapper).readValueAs(BillVO.class);
        assertEquals(BillStatus.PAID, bill.getStatus());
        assertTrue(bill.getIsLocked());
        System.out.println("Step 9 ✅ 账单缴费成功: 状态=" + bill.getStatusName() + ", 已锁定");

        mockMvc.perform(post("/api/bills/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("账单已缴费，不可重复支付"));
        System.out.println("Step 9 ✅ 已缴账单重复支付拦截成功");
    }

    void testStep10_createAppeal() throws Exception {
        RoomDTO room2 = new RoomDTO();
        room2.setBuildingId(buildingId);
        room2.setRoomNumber("102");
        room2.setFloor(1);
        room2.setOwnerName("李四");
        room2.setSharingArea(new BigDecimal("100.0"));
        room2.setHouseholdCount(2);
        room2.setIsVacant(false);
        String r2Resp = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room2)))
                .andReturn().getResponse().getContentAsString();
        Long room2Id = objectMapper.readTree(r2Resp).get("data").get("id").asLong();

        MeterDTO m2 = new MeterDTO();
        m2.setRoomId(room2Id);
        m2.setMeterCode("E-B001-102");
        m2.setMeterType(MeterType.ELECTRICITY);
        m2.setMultiplier(BigDecimal.ONE);
        m2.setLastReading(new BigDecimal("2000.0"));
        m2.setIsActive(true);
        String m2Resp = mockMvc.perform(post("/api/meters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m2)))
                .andReturn().getResponse().getContentAsString();
        Long meter2Id = objectMapper.readTree(m2Resp).get("data").get("id").asLong();

        MeterReadingDTO mr2 = new MeterReadingDTO();
        mr2.setMeterId(meter2Id);
        mr2.setPeriod(period);
        mr2.setCurrentReading(new BigDecimal("2100.0"));
        mr2.setMeterReader("抄表员小王");
        mockMvc.perform(post("/api/meter-readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mr2)))
                .andExpect(status().isOk());

        BillGenerateDTO genDto = new BillGenerateDTO();
        genDto.setPeriod(period);
        genDto.setMeterType(MeterType.ELECTRICITY);
        mockMvc.perform(post("/api/bills/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genDto)))
                .andExpect(status().isOk());

        String billsResp = mockMvc.perform(get("/api/bills/room/{roomId}/period/{period}", room2Id, period))
                .andReturn().getResponse().getContentAsString();
        com.fasterxml.jackson.databind.JsonNode billsNode = objectMapper.readTree(billsResp).get("data");
        assertTrue(billsNode.isArray() && billsNode.size() > 0);
        Long bill2Id = billsNode.get(0).get("id").asLong();

        AppealDTO appealDto = new AppealDTO();
        appealDto.setBillId(bill2Id);
        appealDto.setAppellant("李四");
        appealDto.setAppellantPhone("13900139000");
        appealDto.setAppealType("用量异常");
        appealDto.setDisputedUsage(new BigDecimal("50.0"));
        appealDto.setDisputedAmount(new BigDecimal("30.0"));
        appealDto.setAppealReason("本月不在家，用电量过高，存在异常");

        String appealResp = mockMvc.perform(post("/api/appeals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appealDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.billId").value(bill2Id))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.statusName").value("待处理"))
                .andExpect(jsonPath("$.data.appealReason").value("本月不在家，用电量过高，存在异常"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        AppealVO appeal = objectMapper.readTree(appealResp).get("data").traverse(objectMapper).readValueAs(AppealVO.class);

        AppealHandleDTO handleDto = new AppealHandleDTO();
        handleDto.setAppealId(appeal.getId());
        handleDto.setStatus(AppealStatus.APPROVED);
        handleDto.setHandlerOpinion("经核实，确实存在抄表错误，同意申诉，账单已重置");
        handleDto.setHandler("物业管理员");

        mockMvc.perform(post("/api/appeals/handle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(handleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.statusName").value("申诉通过"))
                .andExpect(jsonPath("$.data.hibernateLazyInitializer").doesNotExist());

        mockMvc.perform(get("/api/bills/{id}", bill2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UNPAID"))
                .andExpect(jsonPath("$.data.isLocked").value(false));

        System.out.println("Step 10 ✅ 申诉流程验证成功: 申诉已通过, 账单已重置为未缴费且解锁");
    }
}
