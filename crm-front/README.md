# PowerCloudCRM-前端

> 动力云客前端项目是基于 Vue 3 + Vite + Element Plus 构建的现代化客户关系管理系统前端。系统面向销售与市场营销场景，提供客户管理、线索管理、市场活动、数据统计等核心业务模块，并以信息化、数字化方式提升营销销售及客户管理效率。

**作者：zs** | **版本：1.0.0** | **版权：zs**

---

## 目录

- [技术栈](#技术栈)
- [核心特性](#核心特性)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [常用命令](#常用命令)
- [代码规范](#代码规范)
- [API 规范](#api-规范)
- [部署指南](#部署指南)
- [常见问题](#常见问题)
- [贡献指南](#贡献指南)
- [相关文档](#相关文档)

---

## 技术栈

| 分类 | 技术/工具 | 版本 | 说明 |
|------|----------|------|------|
| 基础框架 | Vue 3 | ^3.5.13 | 渐进式 JavaScript 框架 |
| 构建工具 | Vite | ^6.1.0 | 下一代前端构建工具 |
| UI 框架 | Element Plus | ^2.9.4 | 基于 Vue 3 的组件库 |
| 路由 | Vue Router | ^4.5.0 | 官方路由管理器 |
| 状态管理 | Pinia | ^3.0.1 | 新一代状态管理方案 |
| 状态持久化 | pinia-plugin-persistedstate | ^4.2.0 | Pinia 状态持久化插件 |
| HTTP 客户端 | Axios | ^1.7.9 | 基于 Promise 的 HTTP 库 |
| 图表 | ECharts | ^6.0.0 | 数据可视化图表库 |
| PDF 查看 | @tato30/vue-pdf | ^1.11.3 | PDF 预览组件 |
| 拖拽 | SortableJS / vuedraggable | ^1.15.6 / ^4.1.0 | 拖拽排序组件 |
| 图标 | @element-plus/icons-vue | ^2.3.1 | Element Plus 图标库 |
| 加密 | crypto-js | ^4.2.0 | 前端加密工具 |
| 工具库 | uuid / qs | ^11.1.0 / ^6.14.0 | UUID 生成、参数序列化 |
| 图片预览 | v-viewer / viewerjs | ^3.0.11 / ^1.11.7 | 图片查看器 |
| 粒子效果 | particles.vue3 / tsparticles | ^2.12.0 | 粒子动画效果 |
| CSS 预处理 | Sass | ^1.66.1 | SCSS 样式预处理器 |
| 代码规范 | ESLint + Prettier + Stylelint | ^9.23.0 / ^3.5.3 / ^16.17.0 | 代码检查与格式化 |
| 提交校验 | Husky + Commitlint | ^9.1.7 / ^19.8.0 | Git 提交规范校验 |
| 自动导入 | unplugin-auto-import / unplugin-vue-components / unplugin-icons | ^19.1.1 / ^28.4.1 / ^22.1.0 | API、组件、图标自动导入 |

---

## 核心特性

- **现代化技术栈**：Vue 3 + Vite 6，构建速度快，开发体验优秀
- **组件化开发**：基于 Element Plus 二次封装，统一业务组件（ZSList、ZSDetail、ZSTable）
- **自动化导入**：Vue API、Element Plus 组件、图标自动导入，减少样板代码
- **状态持久化**：Pinia 状态持久化，刷新不丢失用户信息与权限数据
- **权限控制**：前端路由权限 + 按钮级权限指令，与后端 RBAC 体系对齐
- **统一 HTTP 封装**：基于 Axios 封装请求拦截、Token 注入、重试机制、统一错误处理
- **字典自动转换**：前端配合后端 `@DictConvert`，自动展示字典文本
- **数据可视化**：集成 ECharts，支持销售漏斗、客户增长趋势、业绩统计等图表
- **Excel 导入导出**：支持客户数据批量导入导出
- **文件上传预览**：支持图片、附件上传与预览
- **代码规范体系**：ESLint + Prettier + Stylelint + Commitlint，保证代码质量
- **响应式布局**：基于 Element Plus 布局系统，适配不同分辨率

---

## 项目结构

```
crm-front/
├── build/                          # 构建相关配置
│   ├── config/                     # 构建环境配置
│   ├── generate/                   # 生成器配置
│   ├── script/                     # 构建脚本
│   └── vite/                       # Vite 插件配置
├── public/                         # 公共静态资源目录
├── src/
│   ├── api/                        # API 接口模块（按业务域拆分）
│   │   ├── index.js                # API 统一导出
│   │   ├── user.js                 # 用户管理接口
│   │   ├── customer.js             # 客户管理接口
│   │   ├── clue.js                 # 线索管理接口
│   │   ├── activities.js           # 市场活动接口
│   │   ├── statistic.js            # 数据统计接口
│   │   ├── login.js                # 登录认证接口
│   │   ├── permission.js           # 权限管理接口
│   │   ├── role.js                 # 角色管理接口
│   │   └── apiExample.js           # API 调用示例
│   ├── assets/                     # 静态资源
│   │   ├── images/                 # 图片资源
│   │   └── styles/                 # 全局样式（SCSS 变量、mixin）
│   │       └── variables.module.scss
│   ├── components/                 # 公共组件
│   │   ├── ZSDetail/               # 详情抽屉组件
│   │   ├── ZSList/                 # 列表页面组件（表格、分页、查询）
│   │   └── ZSTable/                # 表格组件
│   ├── composables/                # 组合式函数
│   │   ├── useTable.js             # 表格逻辑封装
│   │   ├── useForm.js              # 表单逻辑封装
│   │   └── usePermission.js        # 权限校验
│   ├── core/                       # 核心模块
│   │   ├── http.js                 # Axios 封装（拦截器、重试、错误处理）
│   │   ├── http-client.js          # HTTP 客户端实例
│   │   └── copyright.js            # 版权信息
│   ├── directives/                 # 自定义指令
│   │   └── permission.js           # 权限指令（v-auth）
│   ├── plugins/                    # 插件配置
│   │   └── element-plus/           # Element Plus 配置
│   ├── router/                     # 路由配置
│   │   └── index.js                # 路由入口与路由表
│   ├── store/                      # Pinia 状态管理
│   │   ├── index.js                # store 入口
│   │   └── modules/                # 模块化 store
│   │       ├── user.js             # 用户信息
│   │       ├── permission.js       # 权限数据
│   │       └── app.js              # 应用状态
│   ├── utils/                      # 工具函数
│   │   ├── auth.js                 # Token 操作
│   │   ├── dict.js                 # 字典工具
│   │   └── validate.js             # 表单验证
│   ├── view/                       # 页面视图
│   │   ├── layout/                 # 布局组件
│   │   │   ├── Header.vue          # 顶部导航
│   │   │   ├── Sidebar.vue         # 侧边栏菜单
│   │   │   └── TagsView.vue        # 标签页导航
│   │   ├── Login/                  # 登录页
│   │   ├── Dashboard/              # 仪表盘
│   │   ├── Welcome/                # 欢迎页
│   │   ├── NotFound/               # 404 页面
│   │   ├── Permission/             # 权限管理
│   │   ├── Role/                   # 角色管理
│   │   ├── User/                   # 用户管理
│   │   ├── Customer/               # 客户管理
│   │   ├── Clue/                   # 线索管理
│   │   ├── Activities/             # 市场活动
│   │   └── Statistic/              # 数据统计
│   ├── App.vue                     # 根组件
│   └── main.js                     # 入口文件
├── .env.development                # 开发环境变量
├── .env.production                 # 生产环境变量
├── .env.staging                    # 预发布环境变量
├── eslint.config.js                # ESLint Flat Config
├── .eslintrc-auto-import.json      # ESLint 自动导入全局变量
├── .prettierrc.cjs                 # Prettier 配置
├── .stylelintrc.cjs                # Stylelint 配置
├── commitlint.config.js            # Commitlint 配置
├── package.json                    # 依赖描述
├── package-lock.json               # 依赖锁文件
├── vite.config.js                  # Vite 配置
└── index.html                      # HTML 入口
```

---

## 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 20.10.0+ | 前端运行环境 |
| npm | 9.0.0+ | 包管理工具 |
| 浏览器 | Chrome 90+ / Edge 90+ | 前端调试（推荐） |
| IDE | VS Code / WebStorm | 前端开发（推荐） |

---

## 快速开始

### 1. 克隆项目

```bash
git clone <repo-url>
cd PowerCloudCRM/crm-front
```

### 2. 安装依赖

```bash
npm install
```

> **注意**：本项目使用 npm 作为包管理工具，请勿使用 yarn 或 pnpm，以避免 lockfile 不一致。

### 3. 启动开发服务器

```bash
npm run dev
```

开发服务器默认运行在 `http://localhost:9527`

### 4. 访问系统

在浏览器中打开 `http://localhost:9527`，使用测试账号登录（账号信息见项目根目录 `sql/powercloud.sql` 初始化数据）。

---

## 常用命令

| 命令 | 说明 |
|------|------|
| `npm run dev` | 启动前端开发服务器（端口 9527） |
| `npm run start` | 同 `npm run dev`，启动开发服务器 |
| `npm run lint` | 前端代码格式化（ESLint + Prettier + Stylelint） |
| `npm run lint:eslint` | 仅运行 ESLint 检查与自动修复 |
| `npm run lint:prettier` | 仅运行 Prettier 格式化 |
| `npm run lint:stylelint` | 仅运行 Stylelint 检查与自动修复 |
| `npm run build` | 前端生产构建 |
| `npm run build:prod` | 生产环境构建 |
| `npm run build:stage` | 预发布环境构建 |
| `npm run preview` | 预览生产构建结果 |

---

## 代码规范

本项目前端代码规范以 **ESLint** 为最高准则，Prettier 和 Stylelint 作为补充。

### 核心规范

| 规则 | 说明 |
|------|------|
| 缩进 | 2 空格 |
| 引号 | 单引号 `'` |
| 分号 | 使用分号（`.js/.ts/.vue`） |
| 尾逗号 | 不使用 |
| 每行最大长度 | 100 字符 |
| 换行符 | `auto`（保持原样） |
| 箭头函数参数 | 始终使用括号 `(x) => x` |
| 对象括号空格 | `{ foo: bar }` |
| 命名 | 驼峰命名（变量/函数），PascalCase（组件） |

### Vue 规范

- 模板中使用 PascalCase 组件名：`<UserCard />`
- 无内容组件必须自闭合
- 单行属性上限：6 个
- 多行属性：每行 1 个
- 属性顺序：`DEFINITION` → `LIST_RENDERING` → `CONDITIONALS` → `EVENTS` → `CONTENT`

### SCSS 规范

遵循 `stylelint-config-recess-order`，属性顺序：
1. 定位（position, top, left, z-index）
2. 盒模型（display, width, height, margin, padding, border）
3. 排版（font, line-height, color, text-align）
4. 视觉（background, opacity, transform）

### Git 提交规范

采用 [Conventional Commits](https://www.conventionalcommits.org/) 规范，通过 Commitlint 强制校验：

```
<type>(<scope>): <subject>
```

**type 类型：** `feat` | `fix` | `docs` | `style` | `refactor` | `perf` | `test` | `chore` | `revert` | `build`

**scope 可选值：** `user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

**示例：**
```
feat(user): 添加用户导出功能
fix(customer): 修复客户列表分页查询错误
style: 代码格式化
```

> 详细规范请参见 [../CODING_STANDARDS.md](../CODING_STANDARDS.md) 和 [CODING_STANDARDS.md](./CODING_STANDARDS.md)

---

## API 规范

### 统一使用 http 模块

所有 API 请求必须通过 `src/core/http.js` 发起，禁止直接使用 `axios`。

```js
import http from '@/core/http.js'

/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 * @param {string} [params.keyword] - 搜索关键词
 * @returns {Promise<{code: number, msg: string, data: Object}>}
 */
export function getUserList(params) {
  return http.get('/api/users', params, {
    operaName: '获取用户列表'
  })
}
```

### 请求方法

| 方法 | 说明 | 示例 |
|------|------|------|
| `http.get(url, params, options)` | GET 请求 | `http.get('/api/users', { page: 1 })` |
| `http.post(url, data, options)` | POST 请求 | `http.post('/api/user/add', userForm)` |
| `http.put(url, data, options)` | PUT 请求 | `http.put('/api/user/update', userForm)` |
| `http.delete(url, params, options)` | DELETE 请求 | `http.delete('/api/user/delete', { id: 1 })` |

### 响应格式

所有接口统一返回以下格式：

```json
{
  "code": 200,
  "msg": "成功",
  "data": {}
}
```

分页接口额外返回：

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

---

## 部署指南

### 生产构建

```bash
npm run build:prod
```

构建产物输出到 `dist/` 目录。

### Nginx 配置示例

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

---

## 常见问题

### 1. 启动失败：`Module not found`

```bash
# 删除 node_modules 和 package-lock.json，重新安装
rm -rf node_modules package-lock.json
npm install
```

### 2. 页面空白/白屏

- 检查浏览器控制台是否有跨域错误
- 确认后端服务已启动且可访问
- 检查 `vite.config.js` 中的代理配置

### 3. 自动导入的全局变量报错

项目已配置 `.eslintrc-auto-import.json`，自动导入的全局变量已注册。如果新增自动导入，需同步更新该文件。

### 4. ESLint 与 Prettier 冲突

以 ESLint 配置为准，Prettier 的分号禁用配置仅作为格式化时的参考。提交前确保 ESLint 通过。

---

## 贡献指南

欢迎贡献代码！请阅读以下文档了解如何参与项目：

- [../CONTRIBUTING.md](../CONTRIBUTING.md) — 通用贡献指南总纲
- [../CODING_STANDARDS.md](../CODING_STANDARDS.md) — 项目代码规范总纲
- [CODING_STANDARDS.md](./CODING_STANDARDS.md) — 前端详细规范
- [CONTRIBUTING.md](./CONTRIBUTING.md) — 前端贡献指南

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
- [CODING_STANDARDS.md](./CODING_STANDARDS.md) — 前端详细规范
- [CONTRIBUTING.md](./CONTRIBUTING.md) — 前端贡献指南
- [crm-server/README.md](../crm-server/README.md) — 后端说明

---

## 许可证

本项目为私有项目，未经作者许可不得用于商业用途。

Copyright © 2024 zs. All rights reserved.
