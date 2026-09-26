# crm-server CONTRIBUTING.md

> 本文件说明如何为后端项目（crm-server）贡献代码，所有贡献者（包括 AI）必须遵守。
>
> 通用规范请参见 [../CODING_STANDARDS.md](../CODING_STANDARDS.md) 和 [../CONTRIBUTING.md](../CONTRIBUTING.md)。

---

## 一、环境准备

### 1.1 开发环境

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 17（项目锁定） | Java 运行环境，推荐用 mise 管理 |
| Maven | 3.8+ | 构建工具（使用 Maven Wrapper） |
| mise | 2024 年中以后的版本 | JDK 多版本管理（见 1.2） |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| IDE | IntelliJ IDEA | 后端开发（推荐） |

### 1.2 环境管理（mise）

后端 JDK 由 [mise](https://mise.jdx.dev) 管理，版本清单在根目录 `mise.toml`（`java = "local-jdk17"`）。

```powershell
# 一次性安装（已安装可跳过）
winget install jdx.mise
'mise activate pwsh | Out-String | Invoke-Expression' | Add-Content $PROFILE
```

```bash
mise install                  # 安装锁定的 JDK 17
mise current                  # 查看生效版本
mise exec -- ./mvnw -v        # 验证：Java version 应为 17.x
```

> 首次使用需把本机已安装的 JDK 注册给 mise：`mise link java@local-jdk17 "D:/Utils/Java/jdk-17"`。
> 未激活 shell 时命令加 `mise exec --` 前缀，确保使用锁定版本。

### 1.3 项目克隆

```bash
git clone <repo-url>
cd PowerCloudCRM/crm-server
```

### 1.4 数据库初始化

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS powercloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p powercloud < ../sql/powercloud.sql
```

### 1.5 IDE 配置

1. 打开 `crm-server` 目录
2. 导入为 Maven 项目
3. 配置 JDK 17
4. 安装 Lombok 插件
5. 导入代码格式化配置（项目根目录 `.idea` 或 `codestyle`）

---

## 二、项目启动

### 2.1 配置修改

修改 `src/main/resources/application.yml`：

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

### 2.2 启动服务

```bash
./mvnw spring-boot:run
```

服务默认运行在 `http://localhost:8080`

> 已配置 mise 时 `./mvnw` 自动使用锁定的 JDK 17；未激活 shell 时使用 `mise exec -- ./mvnw spring-boot:run`。

### 2.3 验证启动

```bash
# 访问 API 文档
curl http://localhost:8080/swagger-ui.html

# 健康检查
curl http://localhost:8080/actuator/health
```

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
mise exec -- ./mvnw compile
```

IDE 格式化：`Code` → `Reformat Code`（快捷键：`Ctrl+Alt+L`）

### 4.2 核心规范

- 使用 `@Resource` 注入依赖
- 返回 `R` 对象
- 使用 JavaDoc 注释
- 分页使用 `PageHelperUtils`
- 遵循阿里巴巴 Java 开发手册

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
3. 运行代码检查：`mise exec -- ./mvnw compile`
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
- [ ] 后端代码无编译错误
- [ ] 提交信息符合规范
- [ ] 无敏感信息泄露（密码、密钥等）

---

## 七、AI 生成代码要求

### 7.1 必须遵守

1. **注释要求**：使用 JavaDoc 注释
2. **格式要求**：阿里 Java 开发手册规范
3. **命名要求**：类 UpperCamelCase，方法 lowerCamelCase
4. **架构要求**：Controller → Service → Mapper 分层，统一返回 `R` 对象
5. **安全要求**：不硬编码密码、密钥，不暴露敏感信息

### 7.2 生成前检查

```markdown
## 代码生成检查清单

### 后端
- [ ] JavaDoc 注释完整
- [ ] 使用 @Resource
- [ ] 返回 R 对象
- [ ] 分页使用 PageHelperUtils
- [ ] 写操作有 @Transactional
- [ ] `mise exec -- ./mvnw compile` 通过编译（JDK 17）
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
- JDK: 
- MySQL: 
- Redis: 
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
| **安全** | SQL 注入、敏感信息 |
| **性能** | N+1 查询、内存泄漏 |

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
