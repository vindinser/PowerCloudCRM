# PowerCloudCRM

> 动力云客项目是一款商业的集营销销售为一体的客户关系管理系统，其采用信息化、数字化方式来进行营销销售及客户管理；
>
> 云客即指海量客户，通过技术方式实现的这一套系统，可用于自动化分析销售、市场营销、客户服务等各个流程，建立起以客户为中心的信息化管理，从而支持更加有效的市场营销、销售与服务等各个环节，提高效率，提高效益。

**作者：zs** | **版本：1.0.0** | **版权：zs**

---

## 目录

- [技术栈](#技术栈)
- [系统架构](#系统架构)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [权限体系](#权限体系)
- [数据库设计](#数据库设计)
- [缓存与安全](#缓存与安全)
- [快速开始](#快速开始)
- [常用命令](#常用命令)
- [API 说明](#api-说明)
- [部署指南](#部署指南)
- [常见问题](#常见问题)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

---

## 技术栈

| 层级 | 技术 |
|------|------|
| **前端** | Vue 3 + Vite + Element Plus + Axios + Pinia + Vue Router + SCSS |
| **后端** | Spring Boot 3.4.2 + Spring Security + MyBatis + MySQL + Redis + JWT |
| **构建工具** | Maven（后端）、npm（前端） |
| **其他组件** | EasyExcel、ECharts、PageHelper、Lombok、Husky、Commitlint |
| **代码规范** | ESLint + Prettier + Stylelint（前端）、Alibaba Java Coding Standards（后端） |
| **开发环境** | IntelliJ IDEA、Apifox、MySQL Workbench、Redis Desktop Manager |

---

## 系统架构

### 整体架构

```
┌─────────────────────────────────────────────────────────────────┐
│                         客户端层 (Client)                        │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                   浏览器 (Chrome / Edge)                   │  │
│  │               Vue 3 + Element Plus + Vite                 │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │ HTTP/HTTPS + JWT
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       网关 / 代理层 (Gateway)                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              Nginx / Vite Dev Proxy (开发环境)             │  │
│  │                    静态资源 + API 转发                      │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      应用服务层 (Application)                    │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              Spring Boot 3.4.2 REST API                   │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐  │  │
│  │  │  Controller  │  │   Service   │  │     Mapper      │  │  │
│  │  │   (web/)     │  │ (service/)  │  │   (mapper/)     │  │  │
│  │  └─────────────┘  └─────────────┘  └─────────────────┘  │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐  │  │
│  │  │    Aspect    │  │    Config    │  │     Util        │  │  │
│  │  │ (AOP切面)    │  │  (配置类)    │  │   (工具类)       │  │  │
│  │  └─────────────┘  └─────────────┘  └─────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│    数据层        │ │    缓存层        │ │    其他服务      │
│   (Database)     │ │   (Cache)        │ │                 │
│  ┌────────────┐  │ │  ┌────────────┐ │ │  ┌────────────┐ │
│  │   MySQL    │  │ │  │   Redis    │ │ │  │ EasyExcel  │ │
│  │  8.0+      │  │ │  │  6.0+      │ │ │  │  ECharts   │ │
│  └────────────┘  │ │  └────────────┘ │ │  └────────────┘ │
│                  │ │                 │ │                 │
│  ┌────────────┐  │ │  ┌────────────┐ │ │  ┌────────────┐ │
│  │ powercloud │  │ │  │ JWT Token  │ │ │  │  File      │ │
│  │   Database │  │ │  │   Cache    │ │ │  │  Export    │ │
│  └────────────┘  │ │  └────────────┘ │ │  └────────────┘ │
└─────────────────┘ └─────────────────┘ └─────────────────┘
```

### 技术架构特点

1. **前后端分离**：前端 Vue 3 通过 RESTful API 与后端通信，JWT 无状态认证
2. **RBAC 权限模型**：基于角色的访问控制，细粒度到按钮级权限
3. **数据权限隔离**：AOP 实现行级数据过滤（`@DataScope`）
4. **字典管理**：AOP 实现字典自动转换（`@DictConvert`）
5. **缓存优化**：Redis 缓存 JWT、字典数据，减少数据库查询
6. **响应统一封装**：所有接口返回 `R` 对象，标准化前后端交互

---

## 项目结构

```
PowerCloudCRM/
├── crm-front/                          # 前端项目（Vue 3 + Vite）
│   ├── src/
│   │   ├── api/                        # API 接口模块（按业务域拆分）
│   │   │   ├── index.js                # API 统一导出
│   │   │   ├── user.js                 # 用户管理接口
│   │   │   ├── customer.js             # 客户管理接口
│   │   │   ├── clue.js                 # 线索管理接口
│   │   │   ├── activity.js             # 市场活动接口
│   │   │   ├── statistic.js            # 数据统计接口
│   │   │   └── auth.js                 # 认证授权接口
│   │   ├── assets/                     # 静态资源
│   │   │   ├── styles/                 # 全局样式（SCSS 变量、mixin）
│   │   │   └── images/                 # 图片资源
│   │   ├── components/                 # 公共组件
│   │   │   ├── CommonHeader/           # 通用头部
│   │   │   ├── CommonFooter/           # 通用底部
│   │   │   ├── Pagination/             # 分页组件
│   │   │   └── Upload/                 # 上传组件
│   │   ├── composables/                # 组合式函数
│   │   │   ├── useTable.js             # 表格逻辑封装
│   │   │   ├── useForm.js              # 表单逻辑封装
│   │   │   └── usePermission.js        # 权限校验
│   │   ├── core/                       # 核心模块
│   │   │   ├── http.js                 # Axios 封装（拦截器、重试、错误处理）
│   │   │   ├── directive.js            # 自定义指令
│   │   │   └── permission.js           # 权限指令
│   │   ├── directives/                 # 自定义指令
│   │   ├── plugins/                    # 插件配置
│   │   ├── router/                     # 路由配置
│   │   │   ├── index.js                # 路由入口
│   │   │   └── routes.js               # 路由表
│   │   ├── store/                      # Pinia 状态管理
│   │   │   ├── modules/                # 模块化 store
│   │   │   │   ├── user.js             # 用户信息
│   │   │   │   ├── permission.js       # 权限数据
│   │   │   │   └── app.js              # 应用状态
│   │   │   └── index.js                # store 入口
│   │   ├── utils/                      # 工具函数
│   │   │   ├── auth.js                 # Token 操作
│   │   │   ├── dict.js                 # 字典工具
│   │   │   └── validate.js             # 表单验证
│   │   ├── view/                       # 页面视图
│   │   │   ├── layout/                 # 布局组件
│   │   │   │   ├── Header.vue
│   │   │   │   ├── Sidebar.vue
│   │   │   │   └── TagsView.vue
│   │   │   ├── dashboard/              # 仪表盘
│   │   │   ├── system/                 # 系统管理
│   │   │   │   ├── user/               # 用户管理
│   │   │   │   ├── role/               # 角色管理
│   │   │   │   └── menu/               # 菜单管理
│   │   │   ├── crm/                    # CRM 业务
│   │   │   │   ├── customer/           # 客户管理
│   │   │   │   ├── clue/               # 线索管理
│   │   │   │   ├── activity/           # 市场活动
│   │   │   │   └── statistic/          # 数据统计
│   │   │   └── login/                  # 登录页
│   │   ├── App.vue                     # 根组件
│   │   ├── main.js                     # 入口文件
│   │   └── permission.js               # 权限控制入口
│   ├── public/                         # 公共静态资源
│   ├── build/                          # 构建配置
│   ├── vite/                           # Vite 插件与配置
│   ├── .env.development                # 开发环境变量
│   ├── .env.production                 # 生产环境变量
│   ├── eslint.config.js                # ESLint 配置（Flat Config）
│   ├── .prettierrc.cjs                 # Prettier 配置
│   ├── .stylelintrc.cjs                # Stylelint 配置
│   ├── commitlint.config.js            # Commitlint 配置
│   ├── package.json                    # 依赖描述
│   └── vite.config.js                  # Vite 配置
├── crm-server/                         # 后端项目（Spring Boot）
│   └── src/main/java/com/zs/crmserver/
│       ├── web/                        # Controller 层（REST API）
│       │   ├── system/                  # 系统管理控制器
│       │   │   ├── UserController.java
│       │   │   ├── RoleController.java
│       │   │   └── MenuController.java
│       │   ├── crm/                    # CRM 业务控制器
│       │   │   ├── CustomerController.java
│       │   │   ├── ClueController.java
│       │   │   ├── ActivityController.java
│       │   │   └── StatisticController.java
│       │   └── CommonController.java   # 公共接口（上传、导出等）
│       ├── service/                    # Service 层
│       │   ├── system/                  # 系统管理服务
│       │   ├── crm/                    # CRM 业务服务
│       │   └── impl/                   # 服务实现类
│       ├── mapper/                     # MyBatis Mapper 接口
│       │   ├── system/
│       │   ├── crm/
│       │   └── TUserMapper.java
│       ├── model/                      # 实体类（MyBatis）
│       │   ├── system/
│       │   │   ├── TUser.java
│       │   │   ├── TRole.java
│       │   │   └── TMenu.java
│       │   ├── crm/
│       │   │   ├── TCustomer.java
│       │   │   ├── TClue.java
│       │   │   └── TActivity.java
│       │   └── BaseEntity.java          # 基类（公共字段）
│       ├── query/                      # 查询参数类
│       │   ├── BasePageQuery.java       # 分页基类
│       │   ├── BaseQuery.java           # 查询基类
│       │   └── CustomerQuery.java       # 客户查询参数
│       ├── result/                     # 返回结果类
│       │   ├── R.java                   # 统一响应结果
│       │   ├── PageResponse.java        # 分页响应
│       │   └── LoginUser.java           # 登录用户信息
│       ├── config/                     # 配置类
│       │   ├── SecurityConfig.java      # Spring Security 配置
│       │   ├── MybatisPlusConfig.java   # MyBatis 配置
│       │   ├── RedisConfig.java         # Redis 配置
│       │   ├── CORSConfig.java          # 跨域配置
│       │   └── SwaggerConfig.java       # API 文档配置
│       ├── aspect/                     # AOP 切面
│       │   ├── DictConvertAspect.java   # 字典转换切面
│       │   └── DataScopeAspect.java     # 数据权限切面
│       ├── util/                       # 工具类
│       │   ├── JwtUtils.java            # JWT 工具
│       │   ├── RedisUtils.java          # Redis 工具
│       │   └── PageHelperUtils.java     # 分页工具
│       ├── constants/                  # 常量定义
│       │   ├── HttpStatus.java          # HTTP 状态码
│       │   └── DictType.java            # 字典类型
│       ├── exception/                  # 异常处理
│       │   ├── GlobalExceptionHandler.java
│       │   └── BusinessException.java
│       └── PowerCloudCrmApplication.java # 启动类
├── sql/                                # 数据库脚本
│   └── powercloud.sql                  # 初始化脚本
├── CODING_STANDARDS.md                 # 项目代码规范总纲
├── CONTRIBUTING.md                     # 贡献指南总纲
├── README.md                           # 项目说明（本文件）
└── LICENSE                             # 开源协议
```

---

## 功能模块

### 1. 系统管理模块

#### 1.1 用户管理
- 用户增删改查
- 用户状态启用/禁用
- 角色分配
- 部门管理
- 密码重置

#### 1.2 角色管理
- 角色增删改查
- 权限分配（菜单权限 + 按钮权限）
- 数据权限范围配置

#### 1.3 菜单管理
- 菜单树形结构
- 动态路由生成
- 按钮级权限控制

#### 1.4 字典管理
- 字典类型管理
- 字典数据维护
- 前端自动转换

### 2. CRM 业务模块

#### 2.1 客户管理
- 客户信息 CRUD
- 客户来源统计
- 客户公海池
- 客户跟进记录
- Excel 导入/导出

#### 2.2 线索管理
- 线索录入
- 线索分配
- 线索转化
- 线索回收

#### 2.3 市场活动
- 活动创建与发布
- 活动跟踪
- 活动效果统计
- 活动备注管理

#### 2.4 数据统计
- 销售漏斗图表
- 客户增长趋势
- 活动效果分析
- 员工业绩统计
- ECharts 可视化

### 3. 系统功能

- **登录认证**：JWT + 图形验证码 + 记住我
- **权限控制**：RBAC + 数据权限（行级过滤）
- **操作日志**：AOP 记录关键操作
- **文件管理**：图片/附件上传
- **消息通知**：系统通知 + 业务提醒

---

## 权限体系

### RBAC 模型

```
用户 (User)
  ├── 角色 (Role)
  │     └── 权限 (Permission)
  │           ├── 菜单权限（页面访问）
  │           └── 按钮权限（操作权限）
  └── 数据权限 (DataScope)
        ├── 全部数据权限
        ├── 自定义数据权限
        └── 个人数据权限
```

### 权限标识示例

| 权限标识 | 说明 |
|----------|------|
| `user:list` | 用户列表查询 |
| `user:add` | 新增用户 |
| `user:edit` | 编辑用户 |
| `user:delete` | 删除用户 |
| `customer:list` | 客户列表查询 |
| `customer:add` | 新增客户 |
| `customer:edit` | 编辑客户 |
| `customer:delete` | 删除客户 |
| `clue:list` | 线索列表查询 |
| `activity:list` | 活动列表查询 |
| `statistic:view` | 数据统计查看 |

### 前端权限控制

- **路由权限**：动态生成路由，无权限用户无法访问页面
- **按钮权限**：自定义指令 `v-auth` 控制按钮显隐
- **接口权限**：Spring Security `@PreAuthorize` 控制接口访问

---

## 数据库设计

### 核心表结构

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `t_user` | 用户表 | id, login_act, name, phone, email, status |
| `t_role` | 角色表 | id, name, code, description |
| `t_menu` | 菜单表 | id, parent_id, name, path, component, perms |
| `t_user_role` | 用户角色关联 | user_id, role_id |
| `t_role_menu` | 角色菜单关联 | role_id, menu_id |
| `t_customer` | 客户表 | id, name, phone, source, level, owner_id |
| `t_clue` | 线索表 | id, name, phone, source, status, owner_id |
| `t_activity` | 活动表 | id, name, type, start_time, end_time, status |
| `t_activity_note` | 活动跟进记录 | id, activity_id, note, create_by |

### 设计规范

- 表名前缀 `t_`，字段使用下划线命名
- 公共字段：`id`（主键）、`create_time`、`update_time`、`create_by`、`update_by`、`del_flag`
- 逻辑删除：`del_flag` 字段（0 未删除，1 已删除）

---

## 缓存与安全

### 缓存策略

- **JWT 存储**：Redis 缓存用户登录状态，7 天过期
- **字典缓存**：字典数据缓存到 Redis，减少数据库查询
- **权限缓存**：用户权限列表缓存，提升接口响应速度

### 安全机制

- **认证方式**：JWT（JSON Web Token）
- **密码加密**：BCrypt 加密存储
- **接口保护**：Spring Security + JWT 过滤器
- **跨域处理**：CORS 配置允许前端域名访问
- **防重复提交**：Token 机制 + 前端按钮防抖

---

## 快速开始

### 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 20.10.0+ | 前端运行环境 |
| JDK | 17+ | 后端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| IDE | IntelliJ IDEA | 后端开发（推荐） |
| 浏览器 | Chrome 90+ | 前端调试（推荐） |

### 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS powercloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入初始化脚本
mysql -u root -p powercloud < sql/powercloud.sql
```

### 后端启动

```bash
# 进入后端目录
cd crm-server

# 修改数据库配置（src/main/resources/application.yml）
# 编辑以下配置项：
# spring:
#   datasource:
#     url: jdbc:mysql:<SECRET_08fc431b>    # MySQL 连接地址
#     username: root                  # 用户名
#     password: <SECRET_c288aaa9>  # 密码
#   data:
#     redis:
#       host: localhost              # Redis 地址
#       port: 6379                   # Redis 端口

# 构建并启动
./mvnw clean install
./mvnw spring-boot:run
```

服务默认运行在 `http://localhost:8080`，API 文档（Swagger）访问 `http://localhost:8080/swagger-ui.html`

### 前端启动

```bash
# 进入前端目录
cd crm-front

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

开发服务器运行在 `http://localhost:9527`

### 验证启动

1. 访问前端 `http://localhost:9527`，应看到登录页
2. 输入测试账号（见 `sql/powercloud.sql` 中的初始化数据）
3. 登录后进入系统主页

---

## 常用命令

### 前端（crm-front）

| 命令 | 说明 |
|------|------|
| `npm run dev` | 启动前端开发服务器（端口 9527） |
| `npm run lint` | 前端代码格式化（ESLint + Prettier + Stylelint） |
| `npm run lint:eslint` | 仅运行 ESLint 检查 |
| `npm run lint:prettier` | 仅运行 Prettier 格式化 |
| `npm run lint:stylelint` | 仅运行 Stylelint 检查 |
| `npm run build` | 前端生产构建 |
| `npm run build:prod` | 生产环境构建 |
| `npm run build:stage` | 预发布环境构建 |
| `npm run preview` | 预览生产构建结果 |

### 后端（crm-server）

| 命令 | 说明 |
|------|------|
| `./mvnw spring-boot:run` | 启动后端服务（端口 8080） |
| `./mvnw clean install` | 清理并构建项目 |
| `./mvnw test` | 运行测试 |
| `./mvnw clean package` | 打包为 JAR |

### 代码规范检查

| 命令 | 说明 |
|------|------|
| `cd crm-front && npm run lint` | 前端代码格式检查与自动修复 |
| `cd crm-server`（IDEA） | Code → Reformat Code（后端格式化） |

---

## API 说明

### 统一响应格式

所有接口统一返回 `R` 对象：

```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | Integer | 状态码（200 成功，其他失败） |
| `msg` | String | 消息提示 |
| `data` | Object/Array | 响应数据 |

### 分页响应格式

分页接口额外返回分页信息：

```json
{
  "code": 200,
  "msg": "成功",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

### 接口前缀

所有 API 统一前缀：`/api`

### 认证方式

- 登录后获取 JWT Token
- 后续请求在 Header 中携带：`Authorization: Bearer <token>`
- Token 有效期 7 天，Redis 中存储

### 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未认证（Token 过期或无效） |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户名或密码错误 |
| 1002 | 验证码错误 |
| 1003 | 用户已被禁用 |

---

## 部署指南

### 前端部署

```bash
# 1. 构建生产版本
cd crm-front
npm run build:prod

# 2. 将 dist/ 目录上传到服务器
# 3. 使用 Nginx 配置静态资源服务
```

Nginx 配置示例：

```nginx
server {
    listen 80;
    server_name crm.example.com;
    root /path/to/crm-front/dist;
    index index.html;

    # SPA 路由支持
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API 代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 后端部署

```bash
# 1. 构建 JAR 包
cd crm-server
./mvnw clean package -DskipTests

# 2. 运行 JAR
java -jar target/crm-server-1.0.0.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### Docker 部署（推荐）

```dockerfile
# 后端 Dockerfile 示例
FROM openjdk:17-jdk-slim
COPY target/crm-server-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```yaml
# docker-compose.yml 示例
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
    build: ./crm-server
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis

  frontend:
    build: ./crm-front
    ports:
      - "80:80"
    depends_on:
      - backend
```

---

## 常见问题

### 1. 前端启动失败：`Module not found`

```bash
# 删除 node_modules 和 package-lock.json，重新安装
rm -rf node_modules package-lock.json
npm install
```

### 2. 后端启动失败：`Could not connect to Redis`

检查 Redis 服务是否启动：

```bash
# Linux/Mac
redis-server --daemonize yes

# Windows（WSL）
redis-cli ping  # 应返回 PONG
```

### 3. 前端请求 404

检查 `vite.config.js` 中的代理配置，确保 `/api` 前缀正确转发到后端。

### 4. 登录后页面空白

检查浏览器控制台是否有跨域错误，确认后端 CORS 配置正确。

### 5. 上传文件失败

检查后端 `application.yml` 中的文件上传路径配置，确保目录存在且有写入权限。

---

## 贡献指南

欢迎贡献代码！请阅读以下文档了解如何参与项目：

- [CODING_STANDARDS.md](./CODING_STANDARDS.md) — 项目代码规范总纲
- [CONTRIBUTING.md](./CONTRIBUTING.md) — 通用贡献指南
- [crm-front/CODING_STANDARDS.md](./crm-front/CODING_STANDARDS.md) — 前端详细规范
- [crm-front/CONTRIBUTING.md](./crm-front/CONTRIBUTING.md) — 前端贡献指南
- [crm-server/CODING_STANDARDS.md](./crm-server/CODING_STANDARDS.md) — 后端详细规范
- [crm-server/CONTRIBUTING.md](./crm-server/CONTRIBUTING.md) — 后端贡献指南

### 快速参与步骤

1. Fork 本仓库
2. 创建功能分支：`git checkout -b feature/amazing-feature`
3. 提交更改：`git commit -m 'feat(scope): add amazing feature'`
4. 推送分支：`git push origin feature/amazing-feature`
5. 创建 Pull Request

### 提交信息规范

本项目采用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>

[可选 body]

[可选 footer(s)]
```

**type 类型：** `feat` | `fix` | `docs` | `style` | `refactor` | `perf` | `test` | `chore` | `revert`

**示例：**
```
feat(user): 添加用户导出功能
fix(customer): 修复客户列表分页查询错误
docs: 更新 README 部署指南
```

---

## 许可证

本项目为私有项目，未经作者许可不得用于商业用途。

Copyright © 2024 zs. All rights reserved.
