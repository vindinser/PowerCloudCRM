# crm-front CONTRIBUTING.md

> 本文件说明如何为前端项目（crm-front）贡献代码，所有贡献者（包括 AI）必须遵守。
>
> 通用规范请参见 [../CODING_STANDARDS.md](../CODING_STANDARDS.md) 和 [../CONTRIBUTING.md](../CONTRIBUTING.md)。

---

## 一、环境准备

### 1.1 开发环境

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 20.12.1（项目锁定） | 前端运行环境，由 mise 管理 |
| npm | 9.0.0+ | 包管理工具 |
| mise | 2024 年中以后的版本 | Node 版本管理（见 1.2） |
| 编辑器 | VS Code / WebStorm | 推荐编辑器 |
| 浏览器 | Chrome 90+ | 前端调试（推荐） |

### 1.2 环境管理（mise）

前端运行时由 [mise](https://mise.jdx.dev) 管理，版本清单在根目录 `mise.toml`（`node = "20.12.1"`）。

```powershell
# 一次性安装（已安装可跳过）
winget install jdx.mise
'mise activate pwsh | Out-String | Invoke-Expression' | Add-Content $PROFILE
```

```bash
mise install          # 安装锁定的 Node 20.12.1
mise current          # 查看生效版本
mise exec -- node -v  # 未激活 shell 时验证版本
```

> 未激活 shell 时命令加 `mise exec --` 前缀，确保使用锁定版本。

### 1.3 项目克隆

```bash
git clone <repo-url>
cd PowerCloudCRM/crm-front
```

### 1.4 安装依赖

```bash
npm install
```

> **注意**：本项目使用 npm，请勿使用 yarn 或 pnpm。

---

## 二、项目启动

### 2.1 开发环境

```bash
npm run dev
```

开发服务器默认运行在 `http://localhost:9527`

### 2.2 生产构建

```bash
npm run build:prod
```

构建产物输出到 `dist/` 目录。

### 2.3 环境变量

项目使用 `.env` 文件管理环境变量：

- `.env.development` — 开发环境配置
- `.env.production` — 生产环境配置
- `.env.staging` — 预发布环境配置

> **注意**：`.env.*` 文件已加入 `.gitignore`，禁止提交到代码仓库。

---

## 三、分支管理

### 3.1 分支命名

| 分支类型 | 命名格式 | 示例 |
|----------|----------|------|
| 功能开发 | `feature/<模块>-<功能>` | `feature/user-export` |
| Bug 修复 | `fix/<模块>-<问题>` | `fix/customer-pagination` |
| 热修复 | `hotfix/<问题>` | `hotfix/login-error` |

### 3.2 分支流程

```bash
# 1. 从 develop 拉取最新代码
git checkout develop
git pull origin develop

# 2. 创建功能分支
git checkout -b feature/user-export

# 3. 开发完成后提交
git add .
git commit -m "feat(user): 添加用户导出功能"

# 4. 推送分支
git push origin feature/user-export
```

---

## 四、代码规范

详细规范请参见 [../CODING_STANDARDS.md](../CODING_STANDARDS.md) 和 [CODING_STANDARDS.md](./CODING_STANDARDS.md)。

### 4.1 快速校验

```bash
npm run lint
```

该命令会依次执行：
1. `npm run lint:eslint` — ESLint 检查与自动修复
2. `npm run lint:prettier` — Prettier 格式化
3. `npm run lint:stylelint` — Stylelint 检查与自动修复

日常快速检查（推荐，只跑 ESLint）：

```bash
npx eslint <文件或目录>
```

### 4.2 核心规范（ESLint）

- 单引号，**使用分号**，2 空格缩进
- 变量命名驼峰，组件名 PascalCase
- 使用 JSDoc 注释
- API 调用使用 `src/core/http.js`
- 禁止 `console.log`、`debugger`、`undefined`

### 4.3 Vue 规范

- 模板中使用 PascalCase 组件名
- 无内容组件必须自闭合
- 属性按定义、列表、条件、事件、内容的顺序排列

### 4.4 SCSS 规范

- 遵循 `stylelint-config-recess-order` 属性顺序
- 允许 `v-deep` / `deep` 深度选择器
- 允许 SCSS 变量和嵌套规则

---

## 五、提交规范

### 5.1 Commit Message 格式

```
<type>(<scope>): <subject>
```

### 5.2 type 类型

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

### 5.3 scope 可选值

`user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

### 5.4 示例

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

## 六、Pull Request 流程

### 6.1 创建 PR

1. 从 `develop` 创建功能分支
2. 完成开发并提交（确保提交信息符合规范）
3. 运行代码检查：`npm run lint`
4. 推送到远程：`git push origin feature/xxx`
5. 在 GitLab/GitHub 创建 Pull Request

### 6.2 PR 描述模板

```markdown
## 变更说明
<!-- 简要描述本次变更 -->

## 变更类型
- [ ] 新功能
- [ ] Bug 修复
- [ ] 重构
- [ ] 文档更新
- [ ] 其他

## 测试情况
<!-- 说明如何测试 -->

## 关联 Issue
<!-- 关联的 Issue 编号 -->
```

### 6.3 审核要求

- [ ] 代码符合 [CODING_STANDARDS.md](./CODING_STANDARDS.md)
- [ ] 前端代码通过 `npm run lint`
- [ ] 提交信息符合规范
- [ ] 无敏感信息泄露（密码、密钥等）

---

## 七、AI 生成代码要求

### 7.1 必须遵守

1. **注释要求**：使用 JSDoc 注释
2. **格式要求**：单引号、分号、2 空格缩进
3. **命名要求**：驼峰命名，组件 PascalCase
4. **架构要求**：API 调用使用 `src/core/http.js`
5. **安全要求**：不硬编码密码、密钥，不暴露敏感信息

### 7.2 生成前检查

```markdown
## 代码生成检查清单

### 前端
- [ ] JSDoc 注释完整
- [ ] 使用 http 模块
- [ ] 正确导入 API
- [ ] 无 console.log
- [ ] 单引号、分号
- [ ] 通过 npm run lint
```

---

## 八、Issue 规范

### 8.1 Bug 报告

```markdown
## Bug 描述
<!-- 简要描述问题 -->

## 复现步骤
1. 
2. 
3. 

## 期望行为
<!-- 描述期望的正确行为 -->

## 实际行为
<!-- 描述实际的错误行为 -->

## 环境信息
- OS: 
- Node.js: 
- 浏览器: 
```

### 8.2 功能请求

```markdown
## 功能描述
<!-- 简要描述需要的功能 -->

## 使用场景
<!-- 说明在什么场景下需要此功能 -->

## 期望方案
<!-- 描述期望的实现方式 -->
```

---

## 九、代码审查

### 9.1 审查要点

| 类别 | 检查项 |
|------|--------|
| **功能** | 是否实现需求，边界情况处理 |
| **代码质量** | 命名、结构、可读性 |
| **规范** | 是否符合 CODING_STANDARDS.md |
| **安全** | XSS、敏感信息 |
| **性能** | 页面渲染性能、接口请求次数 |

### 9.2 审查反馈格式

```markdown
## 审查结果

### 问题
- [ ] 问题描述 + 建议修复方式

### 建议
- 可选改进建议

### 结论
- [ ] 通过
- [ ] 需修改后通过
- [ ] 不通过
```

---

## 十、联系方式

- 项目根目录：[../CONTRIBUTING.md](../CONTRIBUTING.md)
- 作者：zs

如有疑问，请在 Issue 中提出。
