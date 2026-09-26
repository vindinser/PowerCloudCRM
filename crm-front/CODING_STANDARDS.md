# crm-front CODING_STANDARDS.md

> 本文件定义前端项目的编码规范，所有前端代码（包括 AI 生成的代码）必须遵守。
>
> 本项目代码规范以 ESLint 为最高准则，Prettier 和 Stylelint 作为补充。

---

## 一、快速校验

```bash
cd crm-front && npm run lint
```

该命令会依次执行：
1. `npm run lint:eslint` — ESLint 检查与自动修复
2. `npm run lint:prettier` — Prettier 格式化
3. `npm run lint:stylelint` — Stylelint 检查与自动修复

---

## 二、ESLint 规则详解

> 配置文件：`eslint.config.js`（Flat Config）

### 2.1 基础格式

| 规则 | 配置值 | 说明 |
|------|--------|------|
| 缩进 | `2` | 2 空格缩进 |
| 引号 | `'single'` | 使用单引号 |
| 分号 | `'always'` | **强制使用分号** |
| 尾逗号 | `'never'` | 不允许尾逗号 |
| 每行最大长度 | `80`（ESLint）/ `100`（Prettier） | 字符串最大长度 |
| 换行符 | `auto` | 自动识别换行符 |
| 箭头函数括号 | `arrow-parens: 0` | 允许省略括号 |
| 对象括号空格 | `object-curly-spacing: ['never']` | `{foo: bar}` |

### 2.2 命名规范

| 规则 | 配置 | 说明 |
|------|------|------|
| `camelcase` | `error` | 强制驼峰命名 |
| `new-cap` | `error` | 构造函数首字母大写必须使用 `new` |
| `no-underscore-dangle` | `error`（特定允许） | 允许 `this._foo`、方法名下划线及特定变量 |

**允许的下划线前缀变量：**
```js
_id
_name
_value
_data
_props
_computed
_methods
```

**允许的 Vue 大写开头函数（无需 new）：**
```js
Vue
Component
```

### 2.3 强制规则（error 级别）

| 规则 | 配置 | 说明 |
|------|------|------|
| `eqeqeq` | `['error', 'allow-null']` | 使用 `===`（允许 `== null`） |
| `curly` | `['error', 'all']` | 块语句必须使用花括号 |
| `no-console` | `error` | 禁止使用 `console` |
| `no-debugger` | `error` | 禁止使用 `debugger` |
| `no-undefined` | `error` | 禁止使用 `undefined`（用 `void 0` 或明确赋值） |
| `radix` | `2` | `parseInt` 必须指定基数 |
| `default-case` | `error` | `switch` 必须有 `default` 分支 |
| `no-param-reassign` | `error` | 禁止给参数重新赋值 |
| `no-redeclare` | `error` | 禁止重复声明变量 |
| `no-unused-vars` | `error` | 禁止未使用的变量 |
| `no-void` | `error` | 禁用 `void` 操作符 |
| `no-new-object` | `error` | 禁止 `new Object()` |
| `no-new-wrappers` | `error` | 禁止 `new String()` / `new Boolean()` / `new Number()` |
| `no-proto` | `error` | 禁止 `__proto__` |
| `no-with` | `error` | 禁用 `with` 语句 |
| `strict` | `error` | 使用严格模式 |
| `no-else-return` | `error` | 如果 `if` 语句有 `return`，后面不能跟 `else` |
| `no-lonely-if` | `error` | 禁止 `else` 语句内只有 `if` 语句 |

### 2.4 禁止使用的语法

| 规则 | 配置 | 说明 |
|------|------|------|
| `no-alert` | `0` | 允许（但不推荐）`alert` / `confirm` / `prompt` |
| `no-eval` | `error` | 禁止 `eval` / `new Function` |
| `no-implied-eval` | `error` | 禁止隐式 `eval` |
| `no-script-url` | `0` | 禁止 `javascript:void(0)` |
| `no-caller` | `error` | 禁止 `arguments.caller` / `arguments.callee` |
| `no-iterator` | `error` | 禁止 `__iterator__` |
| `no-label-var` | `error` | label 名不能与变量同名 |
| `no-labels` | `error` | 禁止标签声明 |
| `no-sequences` | `0` | 允许逗号运算符 |
| `no-throw-literal` | `error` | 禁止抛出字面量错误 |

### 2.5 代码风格建议

| 规则 | 配置 | 说明 |
|------|------|------|
| `brace-style` | `['error', '1tbs', { allowSingleLine: true }]` | Java 风格花括号 |
| `comma-style` | `['error', 'last']` | 逗号在行尾 |
| `comma-spacing` | `['error', { before: false, after: true }]` | 逗号后空格 |
| `dot-location` | `['error', 'property']` | 点号与属性在同一行 |
| `dot-notation` | `['error', { allowKeywords: true }]` | 使用点号取属性 |
| `key-spacing` | `0` | 对象字面量中冒号前后空格（不强制） |
| `operator-linebreak` | `['error', 'after']` | 换行时运算符在行尾 |
| `padded-blocks` | `0` | 块语句内行首行尾不强制空行 |
| `space-before-blocks` | `0` | 不以新行开始的块前不强制空格 |
| `space-in-parens` | `['error', 'never']` | 小括号内不允许空格 |
| `space-infix-ops` | `0` | 中缀操作符周围不强制空格 |
| `spaced-comment` | `0` | 注释风格不强制空格 |

---

## 三、Prettier 配置

> 配置文件：`.prettierrc.cjs`

Prettier 作为 ESLint 的补充，主要负责代码格式化：

| 配置项 | 值 | 说明 |
|--------|----|------|
| `printWidth` | `100` | 每行最大长度 |
| `tabWidth` | `2` | 缩进空格数 |
| `useTabs` | `false` | 使用空格缩进 |
| `semi` | `true`（默认）/ `false`（`.js/.ts/.vue`） | 分号 |
| `singleQuote` | `true` | 单引号 |
| `trailingComma` | `'none'` | 无尾逗号 |
| `bracketSpacing` | `true` | 对象括号空格 |
| `arrowParens` | `'always'` | 箭头函数参数始终使用括号 |
| `vueIndentScriptAndStyle` | `true` | 缩进 Vue 文件中的 `<script>` 和 `<style>` |
| `endOfLine` | `'auto'` | 自动识别换行符 |

> **注意**：Prettier 对 `.js/.ts/.vue` 文件禁用分号，但 ESLint 配置强制要求分号。**以 ESLint 为准**，Prettier 的分号配置仅在其他文件类型生效。

---

## 四、Stylelint 配置

> 配置文件：`.stylelintrc.cjs`

### 4.1 继承规范

- `stylelint-config-standard`
- `stylelint-config-recommended-scss`
- `stylelint-config-recommended-vue/scss`
- `stylelint-config-html/vue`
- `stylelint-config-recess-order`

### 4.2 SCSS 属性顺序

遵循 `stylelint-config-recess-order`，强制属性顺序：

1. **定位**：`position`, `top`, `right`, `bottom`, `left`, `float`, `display`
2. **盒模型**：`margin`, `padding`, `border`, `border-radius`, `outline`, `width`, `height`
3. **排版**：`font`, `font-size`, `line-height`, `color`, `text-align`, `text-decoration`
4. **视觉**：`background`, `opacity`, `transform`, `z-index`, `visibility`, `cursor`

### 4.3 允许的特殊语法

- `global` / `export` 伪类（Scoped CSS）
- `v-deep` / `deep` 伪类（深度选择器）
- SCSS 变量（`$variable`）
- SCSS 嵌套规则（`@mixin`, `@include`, `@if`, `@else`）

---

## 五、Vue 规范

### 5.1 组件命名

```vue
<!-- ✅ 正确：PascalCase -->
<UserCard />
<CommonHeader />

<!-- ❌ 错误：kebab-case -->
<user-card />
```

### 5.2 自闭合标签

```vue
<!-- ✅ 正确：无内容组件自闭合 -->
<UserCard />

<!-- ❌ 错误：非自闭合 -->
<UserCard></UserCard>
```

### 5.3 属性顺序

Vue 模板属性按以下顺序排列：

1. `DEFINITION` — 定义类（`id`, `ref`, `key`）
2. `LIST_RENDERING` — 列表渲染（`v-for`）
3. `CONDITIONALS` — 条件渲染（`v-if`, `v-show`, `v-else-if`, `v-else`）
4. `RENDER_MODIFIERS` — 渲染修饰（`v-once`, `v-memo`）
5. `GLOBAL` — 全局属性（`id`）
6. `UNIQUE` — 唯一属性（`ref`, `key`）
7. `TWO_WAY_BINDING` — 双向绑定（`v-model`）
8. `OTHER_DIRECTIVES` — 其他指令（`v-bind`, `v-on`）
9. `OTHER_ATTR` — 其他属性
10. `EVENTS` — 事件（`@click`, `v-on`）
11. `CONTENT` — 内容（`v-text`, `v-html`）

### 5.4 属性换行

```vue
<!-- 单行：最多 6 个属性 -->
<el-button type="primary" size="small" @click="handleClick">点击</el-button>

<!-- 多行：每行 1 个属性，垂直对齐 -->
<el-button
  type="primary"
  size="small"
  :disabled="loading"
  @click="handleClick"
>
  点击
</el-button>
```

### 5.5 Script 和 Style 缩进

```vue
<template>
  <div class="user-card">
    <span>{{ user.name }}</span>
  </div>
</template>

<script setup>
// script 块缩进（vueIndentScriptAndStyle: true）
const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})
</script>

<style lang="scss" scoped>
// style 块缩进
.user-card {
  font-size: 14px;
}
</style>
```

---

## 六、API 调用规范

### 6.1 统一使用 http 模块

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

### 6.2 请求方法

| 方法 | 说明 | 示例 |
|------|------|------|
| `http.get(url, params, options)` | GET 请求 | `http.get('/api/users', { page: 1 })` |
| `http.post(url, data, options)` | POST 请求 | `http.post('/api/user/add', userForm)` |
| `http.put(url, data, options)` | PUT 请求 | `http.put('/api/user/update', userForm)` |
| `http.delete(url, params, options)` | DELETE 请求 | `http.delete('/api/user/delete', { id: 1 })` |

### 6.3 参数要求

- `operaName`：必填，用于操作日志记录
- 请求参数必须使用对象形式传递
- 禁止在 URL 中拼接参数

---

## 七、JSDoc 注释规范

### 7.1 函数注释

```js
/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 * @param {string} [params.keyword] - 搜索关键词（可选）
 * @returns {Promise<{code: number, msg: string, data: Object}>}
 */
export function getUserList(params) {
  return http.get('/api/users', params, {
    operaName: '获取用户列表'
  })
}
```

### 7.2 组件注释

```vue
<script setup>
/**
 * 用户卡片组件
 * @description 显示用户头像和姓名，支持点击跳转
 * @example <UserCard :user="userInfo" />
 */

const props = defineProps({
  /** 用户信息对象 */
  user: {
    type: Object,
    required: true
  }
})
</script>
```

### 7.3 模块注释

```js
/**
 * 用户管理模块
 * @module api/user
 */
```

---

## 八、Git 提交规范

### 8.1 Commit Message 格式

```
<type>(<scope>): <subject>
```

### 8.2 type 类型

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(user): 添加用户导出功能` |
| `fix` | Bug 修复 | `fix(customer): 修复客户列表分页查询错误` |
| `docs` | 文档 | `docs: 更新 README` |
| `style` | 格式调整 | `style: 代码格式化` |
| `refactor` | 重构 | `refactor(activity): 重构活动查询逻辑` |
| `perf` | 性能优化 | `perf: 优化客户列表查询` |
| `test` | 测试 | `test: 添加用户服务测试` |
| `chore` | 工具/构建 | `chore: 更新依赖版本` |
| `revert` | 回滚 | `revert: 回滚用户导出功能` |
| `build` | 构建打包 | `build: 更新打包配置` |

### 8.3 scope 可选值

`user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

### 8.4 示例

```
feat(user): 添加用户导出功能
fix(customer): 修复客户列表分页查询错误
refactor(activity): 重构活动查询逻辑，使用 PageHelper
docs: 更新 README 部署指南
style: 代码格式化
perf: 优化客户列表查询性能
test: 添加用户服务测试
chore: 更新依赖版本
```

---

## 九、AI 生成代码规范

### 9.1 生成前检查

生成前端代码前，AI 必须确认：

- [ ] 使用单引号，使用分号
- [ ] 2 空格缩进
- [ ] 变量命名驼峰
- [ ] 组件名 PascalCase
- [ ] API 函数有 JSDoc 注释
- [ ] 不使用 `console.log`
- [ ] 使用 `http` 模块发起请求
- [ ] 正确导入 API 模块
- [ ] 通过 `npm run lint` 检查

### 9.2 代码审查要点

AI 生成的代码需重点审查：
- 是否遵循项目现有代码风格
- 是否使用项目约定的工具和组件（Element Plus、Pinia 等）
- 是否存在安全漏洞（XSS、敏感信息暴露等）
- 是否添加了必要的注释和文档
- 是否通过了自动化检查

---

## 十、目录结构规范

### 10.1 页面结构

```
view/
├── layout/              # 布局组件
│   ├── Header.vue
│   ├── Sidebar.vue
│   └── TagsView.vue
├── dashboard/           # 仪表盘
├── system/              # 系统管理
│   ├── user/            # 用户管理
│   │   ├── index.vue    # 列表页
│   │   ├── add.vue      # 新增页
│   │   └── edit.vue     # 编辑页
│   └── role/
├── crm/                 # CRM 业务
│   ├── customer/
│   ├── clue/
│   └── activity/
└── login/               # 登录页
```

### 10.2 API 结构

```
api/
├── index.js              # 统一导出
├── user.js               # 用户管理接口
├── customer.js           # 客户管理接口
├── clue.js               # 线索管理接口
├── activity.js           # 市场活动接口
├── statistic.js          # 数据统计接口
└── auth.js               # 认证授权接口
```

### 10.3 Store 结构

```
store/
├── index.js              # store 入口
└── modules/
    ├── user.js           # 用户信息
    ├── permission.js     # 权限数据
    └── app.js            # 应用状态
```

---

## 十一、命名约定

| 类型 | 规范 | 示例 |
|------|------|------|
| 文件 | kebab-case | `user-card.vue`, `use-table.js` |
| 组件 | PascalCase | `UserCard.vue` |
| 变量 | camelCase | `userList`, `loginUserId` |
| 常量 | UPPER_SNAKE_CASE | `API_BASE_URL`, `MAX_SIZE` |
| 函数 | camelCase | `getUserById()`, `handleSubmit()` |
| 事件 | kebab-case（模板） | `@handle-click` |
| CSS 类 | kebab-case | `.user-card`, `.btn-primary` |
| SCSS 变量 | kebab-case | `$primary-color`, `$border-radius` |

---

## 十二、提交前检查清单

- [ ] 代码已通过 `npm run lint`
- [ ] 无 `console.log` / `debugger`
- [ ] 无敏感信息（密码、密钥、Token）
- [ ] API 调用使用 `http` 模块
- [ ] 新增组件/页面有 JSDoc 注释
- [ ] 提交信息符合 Conventional Commits 规范

---

## 十三、常见问题

### 13.1 ESLint 与 Prettier 冲突

**现象**：ESLint 要求分号，但 Prettier 对 `.js/.ts/.vue` 文件禁用分号。

**解决**：以 ESLint 配置为准，Prettier 的分号禁用配置仅作为格式化时的参考。提交前确保 ESLint 通过。

### 13.2 自动导入的全局变量报错

**现象**：`ref`、`computed` 等 Vue API 报 `no-undef` 错误。

**解决**：项目已配置 `.eslintrc-auto-import.json`，自动导入的全局变量已注册。如果新增自动导入，需同步更新该文件。

### 13.3 Stylelint 报 SCSS 变量错误

**现象**：`$variable` 被 Stylelint 报错。

**解决**：已配置 `scss/dollar-variable-pattern: null` 允许 SCSS 变量。如果仍有报错，检查 Stylelint 版本。

---

## 十四、更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-01-01 | 初始版本，基于 ESLint Flat Config |
