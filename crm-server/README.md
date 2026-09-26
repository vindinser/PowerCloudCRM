# PowerCloudCRM-后端

> 动力云客后端项目是基于 Spring Boot 3.4.2 + Spring Security + MyBatis 构建的企业级客户关系管理系统服务端。系统面向销售与市场营销场景，提供用户管理、角色权限、客户管理、线索管理、市场活动、数据统计等核心业务接口，并以信息化、数字化方式支撑前端业务的高效运行。

**作者：zs** | **版本：1.0.0** | **版权：zs**

---

## 目录

- [技术栈](#技术栈)
- [核心特性](#核心特性)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [核心机制说明](#核心机制说明)
- [API 说明](#api-说明)
- [数据权限与字典](#数据权限与字典)
- [安全机制](#安全机制)
- [缓存策略](#缓存策略)
- [部署指南](#部署指南)
- [常见问题](#常见问题)
- [贡献指南](#贡献指南)
- [相关文档](#相关文档)

---

## 技术栈

| 分类 | 技术/工具 | 版本 | 说明 |
|------|----------|------|------|
| 基础框架 | Spring Boot | 3.4.2 | 企业级应用框架 |
| 安全框架 | Spring Security | - | 认证与授权 |
| ORM 框架 | MyBatis | - | 数据持久层框架 |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| 缓存 | Redis | 6.0+ | 键值对缓存数据库 |
| 认证机制 | JWT | - | JSON Web Token 无状态认证 |
| 分页插件 | PageHelper | - | MyBatis 分页插件 |
| 工具库 | Lombok | - | 简化 Java 代码 |
| Excel 处理 | EasyExcel | - | Excel 导入导出 |
| 数据校验 | Jakarta Validation | - | 请求参数校验 |
| 构建工具 | Maven Wrapper | - | 项目构建与依赖管理 |
| 运行环境 | JDK | 17+ | Java 开发工具包 |

---

## 核心特性

- **RBAC 权限模型**：基于角色的访问控制，细粒度到按钮级权限，支持数据权限范围配置
- **JWT 无状态认证**：Token 存储在 Redis，7 天有效期，支持记住我功能
- **数据权限隔离**：AOP 实现行级数据过滤（`@DataScope`），不同用户看到的数据范围不同
- **字典自动转换**：AOP 实现字典值自动转换（`@DictConvert`），前端无需手动映射
- **统一响应封装**：所有接口返回 `R` 对象，标准化前后端交互
- **分页查询标准化**：`BasePageQuery` + `PageHelperUtils.pageQuery()`，统一分页逻辑
- **全局异常处理**：`@RestControllerAdvice` 统一捕获异常，返回友好错误信息
- **操作日志**：AOP 记录关键操作日志
- **Excel 导入导出**：基于 EasyExcel 实现客户数据批量导入导出
- **跨域支持**：CORS 配置，支持前后端分离部署
- **接口文档**：集成 Swagger/OpenAPI，自动生成 API 文档

---

## 项目结构

```
crm-server/
└── src/main/java/com/zs/crmserver/
    ├── CrmServerApplication.java    # 启动类
    ├── web/                         # Controller 层（REST API）
    │   ├── UserController.java      # 用户管理
    │   ├── RoleController.java      # 角色管理
    │   ├── PermissionController.java # 权限管理
    │   ├── CustomerController.java  # 客户管理
    │   ├── ClueController.java      # 线索管理
    │   ├── ActivitiesController.java # 市场活动
    │   ├── ActivityRemarkController.java # 活动跟进记录
    │   ├── ClueTrackRecordController.java # 线索跟进记录
    │   ├── StatisticController.java # 数据统计
    │   └── DicController.java       # 字典管理
    ├── service/                     # Service 层
    │   ├── UserService.java         # 用户服务接口
    │   ├── RoleService.java         # 角色服务接口
    │   ├── CustomerService.java     # 客户服务接口
    │   ├── ClueService.java         # 线索服务接口
    │   ├── ActivityService.java     # 活动服务接口
    │   ├── StatisticService.java    # 统计服务接口
    │   ├── DicTypeService.java      # 字典服务接口
    │   ├── ProductService.java      # 产品服务接口
    │   ├── RedisService.java        # Redis 服务
    │   └── impl/                    # Service 实现类
    │       ├── UserServiceImpl.java
    │       ├── RoleServiceImpl.java
    │       └── ...
    ├── mapper/                      # MyBatis Mapper 接口
    │   ├── TUserMapper.java
    │   ├── TRoleMapper.java
    │   ├── TCustomerMapper.java
    │   ├── TClueMapper.java
    │   ├── TActivityMapper.java
    │   └── ...
    ├── model/                       # 实体类（MyBatis）
    │   ├── TUser.java               # 用户实体
    │   ├── TRole.java               # 角色实体
    │   ├── TPermission.java         # 权限实体
    │   ├── TCustomer.java           # 客户实体
    │   ├── TClue.java               # 线索实体
    │   ├── TActivity.java           # 活动实体
    │   ├── TActivityRemark.java     # 活动备注实体
    │   ├── TClueRemark.java         # 线索备注实体
    │   ├── TCustomerRemark.java     # 客户备注实体
    │   ├── TTran.java               # 交易实体
    │   ├── TTranHistory.java        # 交易历史实体
    │   ├── TTranRemark.java         # 交易备注实体
    │   ├── TDicType.java            # 字典类型实体
    │   ├── TDicValue.java           # 字典值实体
    │   ├── TProduct.java            # 产品实体
    │   ├── TSystemInfo.java         # 系统信息实体
    │   ├── TUserRole.java           # 用户角色关联
    │   └── TRolePermission.java     # 角色权限关联
    ├── query/                       # 查询参数类
    │   ├── BasePageQuery.java       # 分页查询基类
    │   ├── BaseQuery.java           # 查询基类
    │   ├── UserQuery.java           # 用户查询参数
    │   ├── CustomerQuery.java       # 客户查询参数
    │   ├── ClueQuery.java           # 线索查询参数
    │   ├── ActivityQuery.java       # 活动查询参数
    │   └── ...
    ├── result/                      # 返回结果类
    │   ├── R.java                   # 统一响应结果
    │   ├── PageResponse.java        # 分页响应
    │   ├── LoginUser.java           # 登录用户信息
    │   ├── CodeEnum.java            # 状态码枚举
    │   ├── DicEnum.java             # 字典枚举
    │   ├── NameValue.java           # 名称值对
    │   ├── SummaryData.java         # 汇总数据
    │   └── CustomerExcel.java       # 客户 Excel 导出模型
    ├── config/                      # 配置类
    │   ├── SecurityConfig.java      # Spring Security 配置
    │   ├── converter/               # 类型转换器
    │   ├── filter/                  # 过滤器（JWT 过滤器等）
    │   ├── handler/                 # 全局异常处理器
    │   └── listener/                # 监听器
    ├── aspect/                      # AOP 切面
    │   ├── DictConvertAspect.java   # 字典转换切面
    │   └── DataScopeAspect.java     # 数据权限切面
    ├── util/                        # 工具类
    │   ├── JwtUtils.java            # JWT 工具
    │   ├── RedisUtils.java          # Redis 工具
    │   ├── PageHelperUtils.java     # 分页工具
    │   ├── PageResponseUtils.java   # 分页响应工具
    │   ├── ResponseUtils.java       # 响应工具
    │   ├── CacheUtils.java          # 缓存工具
    │   ├── DictConversionUtil.java  # 字典转换工具
    │   ├── JSONUtils.java           # JSON 工具
    │   ├── SqlUtils.java            # SQL 工具
    │   └── UserConversionUtil.java  # 用户转换工具
    ├── constants/                   # 常量定义
    │   └── Constants.java           # 系统常量
    ├── manager/                     # 第三方服务管理
    └── task/                        # 定时任务
├── src/main/resources/
│   ├── mapper/                     # MyBatis XML 映射文件
│   │   ├── TUserMapper.xml
│   │   ├── TCustomerMapper.xml
│   │   ├── TClueMapper.xml
│   │   └── ...
│   ├── application.yml             # 主配置文件
│   ├── application-dev.yml         # 开发环境配置
│   ├── application-prod.yml        # 生产环境配置
│   └── logback-spring.xml          # 日志配置
├── src/test/                       # 测试代码
├── pom.xml                         # Maven 依赖配置
└── mvnw                            # Maven Wrapper（Unix）
```

---

## 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17+ | Java 运行环境 |
| Maven | 3.8+ | 构建工具（项目使用 Maven Wrapper） |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| IDE | IntelliJ IDEA | 后端开发（推荐） |
| 接口测试 | Apifox / Postman | API 调试（推荐） |

---

## 快速开始

### 1. 克隆项目

```bash
git clone <repo-url>
cd PowerCloudCRM/crm-server
```

### 2. 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS powercloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p powercloud < ../sql/powercloud.sql
```

### 3. 修改配置

修改 `src/main/resources/application.yml` 中的数据库和 Redis 连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql:<SECRET_08fc431b>    # MySQL 连接地址
    username: root                  # 用户名
    password: <SECRET_c288aaa9>  # 密码
  data:
    redis:
      host: localhost              # Redis 地址
      port: 6379                   # Redis 端口
```

### 4. 启动服务

```bash
./mvnw spring-boot:run
```

服务默认运行在 `http://localhost:8080`

### 5. 验证启动

```bash
# 访问 API 文档（Swagger）
curl http://localhost:8080/swagger-ui.html

# 健康检查
curl http://localhost:8080/actuator/health
```

---

## 核心机制说明

### 统一响应格式

所有接口统一返回 `R` 对象：

```java
// 成功（无数据）
return R.OK();

// 成功（有数据）
return R.OK(data);

// 分页数据
return PageResponseUtils.buildPageResponse(pageInfo);

// 失败
return R.FAIL();

// 失败（自定义消息）
return R.FAIL("用户不存在");
```

响应结构：
```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

### 分页查询标准写法

```java
/**
 * 分页查询用户列表
 *
 * @param query   分页参数（page/size/sort）
 * @param keyword 搜索关键词
 * @return 分页结果
 */
public PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword) {
    return PageHelperUtils.pageQuery(query, () -> userMapper.selectByPage(query, keyword));
}
```

### 权限控制

- 接口级权限：`@PreAuthorize("hasAuthority('user:list')")`
- 按钮级权限：前端自定义指令 `v-auth` 控制
- 数据权限：`@DataScope` 注解实现行级数据过滤

### 异常处理

全局异常处理器捕获所有异常，统一返回 `R` 对象：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return R.FAIL(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return R.FAIL(message);
    }
}
```

---

## API 说明

### 统一前缀

所有 API 统一前缀：`/api`

### 认证方式

- 登录接口获取 JWT Token
- 后续请求在 Header 中携带：`Authorization: Bearer <token>`
- Token 有效期 7 天，Redis 中存储

### 接口文档

项目集成 Swagger，启动后可访问：
- Swagger UI：`http://localhost:8080/swagger-ui.html`

### 常用接口

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 用户管理 | `/api/user/*` | 用户增删改查、角色分配 |
| 角色管理 | `/api/role/*` | 角色增删改查、权限分配 |
| 客户管理 | `/api/customer/*` | 客户 CRUD、导入导出 |
| 线索管理 | `/api/clue/*` | 线索录入、分配、转化 |
| 市场活动 | `/api/activities/*` | 活动创建、跟踪、备注 |
| 数据统计 | `/api/statistic/*` | 销售漏斗、客户增长、业绩统计 |
| 字典管理 | `/api/dic/*` | 字典类型与数据维护 |

---

## 数据权限与字典

### 数据权限（@DataScope）

通过 AOP 切面自动为 SQL 添加数据权限过滤条件，实现行级数据隔离：

- **全部数据权限**：可查看所有数据
- **自定义数据权限**：可查看指定部门/用户的数据
- **个人数据权限**：仅可查看自己的数据

### 字典转换（@DictConvert）

在实体类字段上添加 `@DictConvert` 注解，AOP 自动将字典值转换为对应文本：

```java
@Data
public class TCustomer extends BaseEntity {

    @DictConvert(dicType = "gender", targetField = "genderName")
    private Integer gender;

    @DictConvert(dicType = "customerLevel", targetField = "levelName")
    private Integer level;
}
```

前端无需手动映射，直接返回字典文本。

---

## 安全机制

### 认证与授权

- **认证方式**：JWT（JSON Web Token）
- **密码加密**：BCrypt 加密存储
- **接口保护**：Spring Security + JWT 过滤器链
- **跨域处理**：CORS 配置允许前端域名访问

### 参数校验

使用 Jakarta Validation 进行请求参数校验：

```java
@PostMapping("/add")
public R addUser(@Valid @RequestBody UserQuery query) {
    // 参数校验通过后执行业务逻辑
}
```

### SQL 注入防护

- 使用 `#{param}` 预编译参数
- 禁止拼接 SQL 字符串
- 仅 `filterSQL` 等动态条件使用 `${}`（需谨慎）

---

## 缓存策略

- **JWT 存储**：Redis 缓存用户登录状态，7 天过期
- **字典缓存**：字典数据缓存到 Redis，减少数据库查询
- **权限缓存**：用户权限列表缓存，提升接口响应速度
- **验证码缓存**：图形验证码短期缓存

---

## 部署指南

### 构建 JAR 包

```bash
./mvnw clean package -DskipTests
```

### 运行 JAR

```bash
java -jar target/crm-server-1.0.0.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### Docker 部署示例

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/crm-server-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: <SECRET_c288aaa9>
      MYSQL_DATABASE: powercloud
    volumes:
      - ./sql/powercloud.sql:/docker-entrypoint-initdb.d/powercloud.sql
    ports:
      - "3306:3306"

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  backend:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
```

---

## 常见问题

### 1. 启动失败：`Could not connect to Redis`

检查 Redis 服务是否启动：

```bash
# Linux/Mac
redis-server --daemonize yes
redis-cli ping  # 应返回 PONG

# Windows（WSL）
redis-cli ping
```

### 2. 启动失败：`Communications link failure`

检查 MySQL 服务是否启动，`application.yml` 中的连接信息是否正确。

### 3. 接口返回 401

- 检查 JWT Token 是否过期（7 天有效期）
- 检查请求 Header 中是否携带 `Authorization: Bearer <token>`
- 检查 Redis 中是否存在该 Token

### 4. 接口返回 403

- 检查当前用户是否拥有该接口的权限标识
- 检查 `@PreAuthorize` 中的权限标识是否正确

### 5. 分页参数不生效

确认传入的分页参数为：`page`（页码，从 1 开始）、`size`（每页数量）。

---

## 贡献指南

欢迎贡献代码！请阅读以下文档了解如何参与项目：

- [../CONTRIBUTING.md](../CONTRIBUTING.md) — 通用贡献指南总纲
- [../CODING_STANDARDS.md](../CODING_STANDARDS.md) — 项目代码规范总纲
- [CODING_STANDARDS.md](./CODING_STANDARDS.md) — 后端详细规范
- [CONTRIBUTING.md](./CONTRIBUTING.md) — 后端贡献指南

### 快速参与步骤

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/amazing-feature`
3. 提交更改：`git commit -m 'feat(scope): add amazing feature'`
4. 推送分支：`git push origin feature/amazing-feature`
5. 创建 Pull Request

---

## 相关文档

- [项目根目录 README](../README.md) — 项目整体说明
- [项目根目录 CONTRIBUTING](../CONTRIBUTING.md) — 通用贡献指南
- [项目根目录 CODING_STANDARDS](../CODING_STANDARDS.md) — 代码规范总纲
- [CODING_STANDARDS.md](./CODING_STANDARDS.md) — 后端详细规范
- [CONTRIBUTING.md](./CONTRIBUTING.md) — 后端贡献指南
- [crm-front/README.md](../crm-front/README.md) — 前端说明

---

## 许可证

本项目为私有项目，未经作者许可不得用于商业用途。

Copyright © 2024 zs. All rights reserved.
