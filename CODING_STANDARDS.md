# CODING_STANDARDS.md

> 本文件定义项目的编码规范总纲，所有代码（包括 AI 生成的代码）必须遵守。
>
> 详细规范请参见各模块独立文件：
> - [crm-front/CODING_STANDARDS.md](./crm-front/CODING_STANDARDS.md) — 前端详细规范（以 ESLint 为主）
> - [crm-server/CODING_STANDARDS.md](./crm-server/CODING_STANDARDS.md) — 后端详细规范

---

## 一、通用规范

### 1.1 Git 提交规范

采用 [Conventional Commits](https://www.conventionalcommits.org/) 格式，并通过 Commitlint 强制校验：

```
<type>(<scope>): <subject>

[可选 body]
[可选 footer]
```

**type 类型（必须符合 commitlint 配置）：**

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(user): 添加用户导出功能` |
| `fix` | Bug 修复 | `fix(customer): 修复客户列表分页查询错误` |
| `docs` | 文档变更 | `docs: 更新 README 部署指南` |
| `style` | 代码格式（不影响逻辑） | `style: 代码格式化` |
| `refactor` | 重构（非新功能/非修复） | `refactor(activity): 重构活动查询逻辑` |
| `perf` | 性能优化 | `perf: 优化客户列表查询性能` |
| `test` | 测试相关 | `test: 添加用户服务测试` |
| `chore` | 构建/工具变动 | `chore: 更新依赖版本` |
| `revert` | 回滚 | `revert: 回滚用户导出功能` |
| `build` | 构建打包相关 | `build: 更新 Webpack 配置` |

**scope 可选值：** `user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

**示例：**
```
feat(user): 添加用户导出功能
fix(customer): 修复客户列表分页查询错误
refactor(activity): 重构活动查询逻辑，使用 PageHelper
```

### 1.2 注释要求

- **前端**：使用 JSDoc 注释
- **后端**：使用 JavaDoc 注释

### 1.3 语言规范

- 项目文档、注释、日志、用户提示统一使用中文
- 代码中的技术术语保持英文（如 `Customer`, `Activity`, `BasePageQuery`）

---

## 二、前端规范（crm-front）

> 详细规范见 [crm-front/CODING_STANDARDS.md](./crm-front/CODING_STANDARDS.md)
>
> **注意**：前端代码规范以 ESLint 配置 (`eslint.config.js`) 为最高准则，Prettier 和 Stylelint 作为补充。

### 2.1 快速校验

```bash
cd crm-front && npm run lint
```

### 2.2 核心规范（ESLint）

| 规则 | 配置 |
|------|------|
| 缩进 | 2 空格 |
| 引号 | 单引号 `'` |
| 分号 | **使用分号**（`semi: always`） |
| 尾逗号 | 不使用 |
| 每行最大长度 | 100 字符（Prettier） |
| 换行符 | `auto` |
| 箭头函数参数 | 始终使用括号 `(x) => x` |
| 对象括号空格 | `{ foo: bar }` |
| 命名 | 驼峰命名（`camelcase: error`） |
| 相等比较 | 使用 `===`（允许 `== null`） |
| 块语句 | 必须使用花括号 `curly: error` |
| 禁止 | `console`、`debugger`、`undefined`、`new Object()`、`__proto__`、`with` |

### 2.3 Vue 规范

- **组件名**：模板中使用 PascalCase（`vue/component-name-in-template-casing`）
- **自闭合**：无内容的组件必须自闭合 `<UserCard />`
- **单行属性上限**：6 个
- **多行属性**：每行 1 个
- **属性顺序**：`DEFINITION` → `LIST_RENDERING` → `CONDITIONALS` → `EVENTS` → `CONTENT`

### 2.4 SCSS 规范

遵循 `stylelint-config-recess-order`，属性顺序：
1. 定位（position, top, left, z-index）
2. 盒模型（display, width, height, margin, padding, border）
3. 排版（font, line-height, color, text-align）
4. 视觉（background, opacity, transform）

### 2.5 API 调用规范

所有 API 请求必须通过 `src/core/http.js` 发起：

```js
import http from '@/core/http.js'

export function getUserList(params) {
  return http.get('/api/users', params, {
    operaName: '获取用户列表'
  })
}
```

### 2.6 Git 提交规范

前端提交同样遵循 Conventional Commits 规范，通过 Commitlint 校验：

```bash
git commit -m "feat(user): 添加用户导出功能"
```

---

## 三、后端规范（crm-server）

> 详细规范见 [crm-server/CODING_STANDARDS.md](./crm-server/CODING_STANDARDS.md)
>
> 基于阿里巴巴 Java 开发手册。

### 3.1 快速校验

- IDE：IntelliJ IDEA → Code → Reformat Code（基于项目配置）
- 编译：`./mvnw clean install`

### 3.2 核心规范

| 类别 | 规范 |
|------|------|
| 类名 | UpperCamelCase（`UserController`, `CustomerService`） |
| 方法名 | lowerCamelCase（`getUserById()`, `saveUser()`） |
| 常量 | UPPER_SNAKE_CASE（`LOGIN_URI`, `REDIS_JWT_KEY`） |
| 变量 | lowerCamelCase（`loginUserId`, `userList`） |
| 包名 | 全小写（`com.zs.crmserver.web`） |
| 表名前缀 | `t_`（`t_user`, `t_customer`） |
| 字段命名 | 下划线（`login_act`, `create_time`） |
| 依赖注入 | `@Resource`（不用 `@Autowired`） |
| 事务控制 | `@Transactional(rollbackFor = Exception.class)` |
| 权限控制 | `@PreAuthorize("hasAuthority('xxx')")` |
| 统一响应 | 返回 `R` 对象（`R.OK(data)` / `R.FAIL()`） |
| 分页查询 | `PageHelperUtils.pageQuery()` |

### 3.3 分层规范

```
Controller（web/）→ Service（service/）→ Mapper（mapper/）
```

- **Controller**：仅做参数校验和调用 Service，不写业务逻辑
- **Service**：接口 + 实现类，处理业务逻辑
- **Mapper**：MyBatis 接口，对应 XML 中的 SQL

### 3.4 Git 提交规范

后端提交同样遵循 Conventional Commits 规范：

```bash
git commit -m "feat(user): 添加用户导出功能"
```

---

## 四、AI 生成代码检查清单

生成代码前，确认以下事项：

### 前端（crm-front）

- [ ] 使用单引号，**使用分号**（`.js/.ts/.vue`）
- [ ] 2 空格缩进
- [ ] 变量命名驼峰
- [ ] 组件名 PascalCase
- [ ] API 函数有 JSDoc 注释
- [ ] 不使用 `console.log`
- [ ] 使用 `http` 模块发起请求
- [ ] 正确导入 API 模块 `import api from '@/api'`
- [ ] 通过 `npm run lint` 检查

### 后端（crm-server）

- [ ] 使用 `@Resource` 注入依赖
- [ ] 写操作添加 `@Transactional(rollbackFor = Exception.class)`
- [ ] 接口方法有 JavaDoc 注释
- [ ] 返回 `R` 对象
- [ ] 分页使用 `PageHelperUtils.pageQuery()`
- [ ] 权限注解 `@PreAuthorize`
- [ ] 实体类字段有注释
- [ ] Mapper XML 与接口方法名一致
- [ ] 项目通过编译 `./mvnw clean install`

---

## 五、格式化命令

```bash
# 前端（ESLint + Prettier + Stylelint）
cd crm-front && npm run lint

# 后端（IDEA 自带格式化）
# Code → Reformat Code（快捷键：Ctrl+Alt+L）
```

---

## 六、敏感信息规范

- 禁止将密码、密钥、Token 提交到代码仓库
- 使用环境变量或配置中心管理敏感信息
- `.env` 文件已加入 `.gitignore`，禁止提交
- 数据库连接信息必须从 `application.yml` 外部化配置

---

## 七、代码审查要点

| 类别 | 检查项 |
|------|--------|
| **功能** | 是否实现需求，边界情况处理 |
| **代码质量** | 命名、结构、可读性 |
| **规范** | 是否符合 CODING_STANDARDS.md |
| **安全** | SQL 注入、XSS、敏感信息 |
| **性能** | N+1 查询、内存泄漏 |
| **兼容性** | 浏览器兼容性、数据迁移 |

---

## 八、更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-01-01 | 初始版本，基于 ESLint + Alibaba Java Coding Standards |
