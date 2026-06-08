# 物业公司水电抄表分摊服务

基于 Spring Boot 3.x 的水电抄表、公摊计算、账单生成、缴费管理和用量申诉全流程后端服务系统。

## 原始需求

> 物业公司需要水电抄表分摊服务，Spring Boot 接口承接房间表读数、公区表读数、公摊规则、账单生成、缴费状态和用量申诉。业务字段包括楼栋、房号、上期读数、本期读数、倍率、空置状态、公摊面积、租户变更、单价和异常用量。抄表员录入水电表照片和读数，财务按楼栋、面积或户数分摊公共能耗，业主查看账单并发起申诉。服务要区分表倒走、漏抄、空置房、公区异常、租户换约和账单已缴不可改。

## 功能模块

### 1. 基础数据管理
- 楼栋管理
- 房间管理（含空置状态、公摊面积、户数）
- 水电表管理（房间表、公区表）
- 租户管理（租户换约记录）

### 2. 抄表管理
- 房间水电表读数录入（支持照片上传）
- 公区水电表读数录入
- **表倒走检测**：本期读数 < 上期读数自动标记异常
- **异常用量检测**：用量超上期 2 倍自动标记
- **空置房处理**：空置房按特殊状态处理
- **租户换约**：租户变更标记记录

### 3. 公摊规则管理
- 按面积分摊
- 按户数分摊
- 按楼栋均摊
- 自定义分摊规则
- 支持单价和固定金额配置

### 4. 账单管理
- 批量生成账单（按周期、楼栋、表类型）
- 个人用量 + 公摊用量明细
- 缴费状态追踪
- **账单已缴不可改**：已缴费账单锁定不可修改
- 逾期管理

### 5. 申诉管理
- 业主发起用量申诉
- 申诉处理流程（待处理/处理中/通过/驳回）
- 申诉通过后账单重置

## 技术栈

- Java 17
- Spring Boot 3.4.1
- Spring Data JPA
- Spring Validation
- MySQL 8.0
- Lombok
- Jackson

## 启动方式

### 前置要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Docker & Docker Compose（推荐使用 Docker 一键启动）

---

### Docker 一键启动（推荐）

#### 1. 构建并启动服务

```bash
docker compose up --build
```

后台运行：

```bash
docker compose up --build -d
```

#### 2. 停止服务

```bash
docker compose down
```

#### 3. 服务地址

- 应用服务：http://localhost:8080
- MySQL：localhost:3306
  - 数据库：property_meter
  - 用户名：property
  - 密码：property123

---

### 本地开发启动

#### 1. 安装依赖

```bash
mvn clean install -DskipTests
```

#### 2. 配置数据库

创建 MySQL 数据库并修改 `src/main/resources/application.yml` 中的数据库连接配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/property_meter?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
    username: root
    password: your_password
```

#### 3. 启动服务

```bash
mvn spring-boot:run
```

访问地址：http://localhost:8080

---

## API 接口列表

### 楼栋管理 `/api/buildings`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/buildings | 新增楼栋 |
| PUT | /api/buildings/{id} | 更新楼栋 |
| DELETE | /api/buildings/{id} | 删除楼栋 |
| GET | /api/buildings/{id} | 查询楼栋详情 |
| GET | /api/buildings/code/{code} | 按编号查询 |
| GET | /api/buildings | 查询所有楼栋 |

### 房间管理 `/api/rooms`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/rooms | 新增房间 |
| PUT | /api/rooms/{id} | 更新房间 |
| DELETE | /api/rooms/{id} | 删除房间 |
| GET | /api/rooms/{id} | 查询房间详情 |
| GET | /api/rooms/building/{buildingId} | 按楼栋查询 |
| GET | /api/rooms | 查询所有房间 |
| GET | /api/rooms/vacant | 查询空置房 |

### 水电表管理 `/api/meters`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/meters | 新增水电表 |
| PUT | /api/meters/{id} | 更新水电表 |
| DELETE | /api/meters/{id} | 删除水电表 |
| GET | /api/meters/{id} | 查询详情 |
| GET | /api/meters/room/{roomId} | 按房间查询 |

### 抄表管理 `/api/meter-readings`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/meter-readings | 录入抄表读数 |
| PUT | /api/meter-readings/{id} | 更新读数 |
| DELETE | /api/meter-readings/{id} | 删除读数 |
| GET | /api/meter-readings/{id} | 查询详情 |
| GET | /api/meter-readings/period/{period} | 按周期查询 |
| GET | /api/meter-readings/room/{roomId}/period/{period} | 按房间周期查询 |

### 公区表管理 `/api/public-meters`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/public-meters | 新增公区表 |
| GET | /api/public-meters/{id} | 查询公区表 |
| POST | /api/public-meters/readings | 录入公区表读数 |
| GET | /api/public-meters/readings/{id} | 查询公区读数 |

### 公摊规则 `/api/sharing-rules`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/sharing-rules | 新增公摊规则 |
| GET | /api/sharing-rules/active | 查询所有生效规则 |
| GET | /api/sharing-rules/building/{buildingId} | 按楼栋查询 |

### 账单管理 `/api/bills`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/bills/generate | 批量生成账单 |
| POST | /api/bills/pay | 账单缴费 |
| GET | /api/bills/{id} | 查询账单详情 |
| GET | /api/bills/room/{roomId} | 按房间查询 |
| GET | /api/bills/room/{roomId}/unpaid | 查询未缴账单 |
| GET | /api/bills/status/{status} | 按状态查询 |

### 申诉管理 `/api/appeals`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/appeals | 发起申诉 |
| POST | /api/appeals/handle | 处理申诉 |
| GET | /api/appeals/{id} | 查询申诉详情 |
| GET | /api/appeals/room/{roomId} | 按房间查询 |
| GET | /api/appeals/status/{status} | 按状态查询 |

### 租户管理 `/api/tenants`
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/tenants | 新增租户（自动处理换约） |
| GET | /api/tenants/{id} | 查询租户 |
| GET | /api/tenants/room/{roomId}/active | 查询房间当前租户 |

## 业务校验规则

| 规则类型 | 说明 |
|----------|------|
| 表倒走 | 本期读数 < 上期读数，标记 REVERSED 异常 |
| 漏抄 | 周期内未录入读数，生成账单时标记 MISSING |
| 空置房 | 房间空置时，个人用量计 0，公摊按规则计算 |
| 公区异常 | 公区用量超历史均值 2 倍，标记 ABNORMAL |
| 租户换约 | 抄表时标记租户变更，历史用量按租户切割 |
| 账单已缴 | 已缴费账单 isLocked=true，禁止修改和重复缴费 |

## 注意事项

1. 数据库表由 JPA 自动创建（`ddl-auto: update`）
2. 抄表照片字段 photoUrl 支持对接 OSS/文件服务
3. 公摊计算支持按面积/户数/楼栋三种内置方式，自定义公式可通过 customFormula 扩展
4. 默认单价：水 4.5 元/吨，电 0.6 元/度，可通过公摊规则覆盖
