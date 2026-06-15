# PowerCloudCRM

> 动力云客项目是一款商业的集营销销售为一体的客户关系管理系统，其采用信息化、数字化方式来进行营销销售及客户管理；
>
> 云客即指海量客户，通过技术方式实现的这一套系统，可用于自动化分析销售、市场营销、客户服务等各个流程，建立起以客户为中心的信息化管理，从而支持更加有效的市场营销、销售与服务等各个环节，提高效率，提高效益。

**作者：zs** | **版本：1.0.0** | **版权：zs**

---

## 技术栈

| 层级 | 技术 |
|------|------|
| **前端** | Vue 3 + Vite + Element Plus + Axios + Pinia + Vue Router |
| **后端** | Spring Boot 3.4.2 + Spring Security + MyBatis + MySQL + Redis |
| **构建工具** | Maven（后端）、npm（前端） |
| **其他组件** | JWT、EasyExcel、ECharts、PageHelper、Lombok |
| **开发环境** | IDEA、Apifox |

## 项目结构

```
PowerCloudCRM/
├── crm-front/                # 前端项目（Vue 3 + Vite）
│   ├── src/
│   │   ├── api/              # API 接口模块
│   │   ├── assets/           # 静态资源
│   │   ├── components/       # 公共组件
│   │   ├── composables/      # 组合式函数
│   │   ├── core/             # 核心模块（HTTP 客户端等）
│   │   ├── directives/       # 自定义指令
│   │   ├── plugins/          # 插件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # Pinia 状态管理
│   │   └── view/             # 页面视图
│   └── vite/                 # Vite 插件配置
├── crm-server/               # 后端项目（Spring Boot）
│   └── src/main/java/com/zs/crmserver/
│       ├── web/              # Controller 层
│       ├── service/          # Service 层
│       ├── mapper/           # MyBatis Mapper 接口
│       ├── model/            # 实体类
│       ├── query/            # 查询参数类
│       ├── result/           # 返回结果类
│       ├── config/           # 配置类
│       ├── aspect/           # AOP 切面
│       ├── util/             # 工具类
│       └── constants/        # 常量定义
└── sql/                      # 数据库脚本
    └── powercloud.sql
```

## 功能模块

- **用户管理** — 用户增删改查、角色分配、权限控制
- **市场活动** — 活动创建、跟踪、备注管理
- **线索管理** — 线索录入、分配、转化跟踪
- **客户管理** — 客户信息维护、Excel 导入导出
- **数据统计** — ECharts 图表展示、数据分析

## 快速开始

### 环境要求

- Node.js 20.10.0+
- JDK 17+
- MySQL 8.0+
- Redis

### 数据库初始化

```bash
mysql -u root -p powercloud < sql/powercloud.sql
```

### 后端启动

```bash
cd crm-server
# 修改 src/main/resources/application.yml 中的数据库和 Redis 连接信息
./mvnw spring-boot:run
```

服务默认运行在 `http://localhost:8080`

### 前端启动

```bash
cd crm-front
npm install
npm run dev
```

开发服务器运行在 `http://localhost:9527`

## 常用命令

| 命令 | 说明 |
|------|------|
| `npm run dev` | 启动前端开发服务器 |
| `npm run lint` | 前端代码格式化（ESLint + Prettier + Stylelint） |
| `npm run build` | 前端生产构建 |
| `./mvnw spring-boot:run` | 后端启动 |
| `./mvnw clean install` | 后端构建 |
| `./mvnw test` | 运行后端测试 |

## API 说明

所有接口统一前缀 `/api/`，返回格式：

```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

分页接口额外返回 `ListData` 字段。

## 许可证

本项目为私有项目，未经作者许可不得用于商业用途。
