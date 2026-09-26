# CONTRIBUTING.md

> 本文件说明如何为 PowerCloudCRM 项目贡献代码，所有贡献者（包括 AI）必须遵守。
>
> 详细指南请参见各模块独立文件：
> - [crm-front/CONTRIBUTING.md](./crm-front/CONTRIBUTING.md) — 前端贡献指南
> - [crm-server/CONTRIBUTING.md](./crm-server/CONTRIBUTING.md) — 后端贡献指南

---

## 一、环境准备

### 1.1 开发环境

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 20.10.0+ | 前端运行环境 |
| JDK | 17+ | 后端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| IDE | IntelliJ IDEA | 后端开发（推荐） |
| 编辑器 | VS Code / WebStorm | 前端开发（推荐） |
| Git | 2.30+ | 版本控制 |

### 1.2 项目克隆

```bash
git clone <repo-url>
cd PowerCloudCRM
```

### 1.3 数据库初始化

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS powercloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p powercloud < sql/powercloud.sql
```

---

## 二、分支管理

### 2.1 分支命名

| 分支类型 | 命名格式 | 示例 |
|----------|----------|------|
| 功能开发 | `feature/<模块>-<功能>` | `feature/user-export` |
| Bug 修复 | `fix/<模块>-<问题>` | `fix/customer-pagination` |
| 热修复 | `hotfix/<问题>` | `hotfix/login-error` |
| 发布 | `release/<版本号>` | `release/1.1.0` |

### 2.2 分支流程

```
main
  ↑
  └── develop
        ↑
        └── feature/xxx
        └── fix/xxx
```

- `main`：生产环境代码，受保护分支
- `develop`：开发主分支，所有功能/修复分支合并到此
- `feature/*`：功能分支，完成后合并到 `develop`
- `fix/*`：修复分支，完成后合并到 `develop`
- `hotfix/*`：紧急修复，从 `main` 拉出，修复后合并到 `main` 和 `develop`

### 2.3 分支操作规范

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

## 三、代码规范

详见 [CODING_STANDARDS.md](./CODING_STANDARDS.md)，以下为快速摘要：

### 3.1 前端

- 单引号，**使用分号**，2 空格缩进
- 使用 JSDoc 注释
- API 调用使用 `src/core/http.js`
- 组件命名 PascalCase
- ESLint 为主，Prettier + Stylelint 为辅

### 3.2 后端

- 使用 `@Resource` 注入
- 返回 `R` 对象
- 使用 JavaDoc 注释
- 分页使用 `PageHelperUtils`
- 遵循阿里巴巴 Java 开发手册

---

## 四、提交规范

### 4.1 Commit Message 格式

```
<type>(<scope>): <subject>
```

本项目通过 Commitlint 强制校验提交格式，不符合规范将无法提交。

**type 类型：**

| 类型 | 说明 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat(user): 添加用户导出` |
| `fix` | Bug 修复 | `fix(clue): 修复线索分配错误` |
| `docs` | 文档 | `docs: 更新 README` |
| `style` | 格式调整 | `style: 代码格式化` |
| `refactor` | 重构 | `refactor: 重构活动查询` |
| `perf` | 性能优化 | `perf: 优化客户列表查询` |
| `test` | 测试 | `test: 添加用户服务测试` |
| `chore` | 工具/构建 | `chore: 更新依赖版本` |
| `revert` | 回滚 | `revert: 回滚用户导出功能` |
| `build` | 构建打包 | `build: 更新打包配置` |

**scope 可选值：** `user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

### 4.2 提交检查

```bash
# 提交前检查
cd crm-front && npm run lint
cd crm-server && ./mvnw clean install

# 提交
git add .
git commit -m "feat(user): 添加用户导出功能"
```

### 4.3 不规范的提交示例

```bash
# ❌ 错误示例
git commit -m "更新代码"
git commit -m "fix: 修复bug"
git commit -m "feat(user):添加用户导出功能"  # 缺少空格

# ✅ 正确示例
git commit -m "feat(user): 添加用户导出功能"
git commit -m "fix(customer): 修复客户列表分页查询错误"
git commit -m "docs: 更新 README 部署指南"
```

---

## 五、Pull Request 流程

### 5.1 创建 PR

1. 从 `develop` 创建功能分支
2. 完成开发并提交（确保提交信息符合规范）
3. 运行代码检查（`npm run lint` 或 `./mvnw clean install`）
4. 推送到远程：`git push origin feature/xxx`
5. 在 GitLab/GitHub 创建 Pull Request

### 5.2 PR 描述模板

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

### 5.3 审核要求

- [ ] 代码符合 [CODING_STANDARDS.md](./CODING_STANDARDS.md)
- [ ] 前端代码通过 `npm run lint`
- [ ] 后端代码无编译错误
- [ ] 提交信息符合规范
- [ ] 无敏感信息泄露（密码、密钥等）
- [ ] 功能测试通过

---

## 六、AI 生成代码要求

### 6.1 必须遵守

1. **注释要求**
   - 前端：使用 JSDoc 注释
   - 后端：使用 JavaDoc 注释

2. **格式要求**
   - 前端：单引号、分号、2 空格缩进（以 ESLint 为准）
   - 后端：阿里 Java 开发手册规范

3. **命名要求**
   - 前端：驼峰命名，组件 PascalCase
   - 后端：类 UpperCamelCase，方法 lowerCamelCase

4. **架构要求**
   - 前端：API 调用使用 `src/core/http.js`
   - 后端：Controller → Service → Mapper 分层
   - 后端：统一返回 `R` 对象

5. **安全要求**
   - 不硬编码密码、密钥
   - 不暴露敏感信息
   - 使用参数化查询（防 SQL 注入）
   - 禁止将 `.env`、`application.yml` 等配置文件提交

### 6.2 生成前检查

```markdown
## 代码生成检查清单

### 前端
- [ ] JSDoc 注释完整
- [ ] 使用 http 模块
- [ ] 正确导入 API
- [ ] 无 console.log
- [ ] 单引号、分号
- [ ] 通过 npm run lint

### 后端
- [ ] JavaDoc 注释完整
- [ ] 使用 @Resource
- [ ] 返回 R 对象
- [ ] 分页使用 PageHelperUtils
- [ ] 写操作有 @Transactional
- [ ] 项目通过编译
```

### 6.3 AI 代码审查要点

- 是否遵循项目现有代码风格
- 是否使用项目约定的工具和组件
- 是否存在安全漏洞（SQL 注入、XSS 等）
- 是否添加了必要的注释和文档
- 是否通过了自动化检查（ESLint / Maven）

---

## 七、Issue 规范

### 7.1 Bug 报告

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
- JDK: 
- 浏览器: 
```

### 7.2 功能请求

```markdown
## 功能描述
<!-- 简要描述需要的功能 -->

## 使用场景
<!-- 说明在什么场景下需要此功能 -->

## 期望方案
<!-- 描述期望的实现方式 -->
```

### 7.3 提交 Issue 前检查

- [ ] 已搜索现有 Issue，确认问题未被报告
- [ ] 已提供详细的复现步骤
- [ ] 已说明期望行为和实际行为
- [ ] 已提供相关日志或截图

---

## 八、代码审查

### 8.1 审查要点

| 类别 | 检查项 |
|------|--------|
| **功能** | 是否实现需求，边界情况处理 |
| **代码质量** | 命名、结构、可读性 |
| **规范** | 是否符合 CODING_STANDARDS.md |
| **安全** | SQL 注入、XSS、敏感信息 |
| **性能** | N+1 查询、内存泄漏 |
| **兼容性** | 浏览器兼容性、数据迁移 |

### 8.2 审查反馈格式

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

### 8.3 审查流程

1. 代码提交后自动触发 CI 检查
2. 至少 1 名核心开发者 Review
3. Review 通过后合并代码
4. 合并后删除功能分支

---

## 九、联系方式

- 作者：zs
- 项目地址：[PowerCloudCRM](./)

如有疑问，请在 Issue 中提出。
