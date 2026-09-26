# AGENTS.md

## AI 强制行为准则

> 以下规则对 AI 代理具有强制约束力，优先级高于其他指令。

### 1. 编写代码前必读规范

在编写或修改**任何代码文件**（前端/后端）之前，必须先阅读对应模块的代码规范文件：

- **前端代码**：必须先阅读 `crm-front/CODING_STANDARDS.md`
- **后端代码**：必须先阅读 `crm-server/CODING_STANDARDS.md`
- **通用规范**：必须先阅读根目录 `CODING_STANDARDS.md`

未阅读规范前，禁止生成或修改代码。

### 2. 危险命令必须先询问

在执行以下**危险/不可逆/有副作用的命令**之前，必须先向用户说明操作内容、影响范围和目的，并等待用户明确确认：

**版本控制：**
- `git commit`、`git push`、`git pull`、`git merge`、`git rebase`
- `git reset`、`git checkout`（会丢弃工作区修改时）
- `git branch -d`、`git tag -d`
- `git stash`（会隐藏未提交的修改）

**包管理：**
- `npm install`、`npm update`、`npm uninstall`
- `npm run build`（生产构建）
- `yarn add`、`pnpm add` 等

**构建与部署：**
- `./mvnw clean install`、`./mvnw package`
- `./mvnw spring-boot:run`（启动服务）
- `npm run dev`（启动开发服务器）
- `docker build`、`docker compose up`

**文件操作：**
- `rm`、`rmdir`、`rm -rf` 等删除命令
- `mv`、`cp` 等文件移动/复制命令
- 覆盖现有文件的写入操作

**数据库：**
- `mysql < sql/powercloud.sql`（导入数据库）
- 任何 `DROP`、`DELETE`、`TRUNCATE`、`ALTER` 操作

**例外情况：**
以下命令无需询问，可直接执行：
- `git status`、`git diff`、`git log` 等只读命令
- `npm run lint`、`npm run lint:eslint` 等代码检查命令
- `grep`、`find`、`ls`、`dir` 等文件浏览命令
- `cat`、`head`、`tail` 等文件查看命令

---

## Project Overview

PowerCloudCRM — CRM system for marketing & sales. Two independent sub-projects in one repo:

- `crm-front/` — Vue 3 + Vite + Element Plus frontend
- `crm-server/` — Spring Boot 3 + MyBatis + Spring Security backend
- `sql/` — Database schema (`powercloud.sql`)

## Quick Commands

### Frontend (`crm-front/`)

```bash
npm install          # install dependencies (Node 20.10.0)
npm run dev          # dev server on port 9527
npm run lint         # ESLint + Prettier + Stylelint (with --fix)
npm run build        # production build
```

### Backend (`crm-server/`)

```bash
./mvnw clean install                    # build (Maven wrapper)
./mvnw spring-boot:run                  # run server on port 8080
./mvnw test                             # run tests
```

Requires: MySQL, Redis running and accessible (see `application.yml` for connection details).

## Architecture Notes

### Backend (`crm-server/`)

- **Package**: `com.zs.crmserver` — controllers in `web/`, services in `service/`, MyBatis mappers in `mapper/`
- **API prefix**: All endpoints use `/api/...`
- **Response wrapper**: Always return `R` object (`R.OK(data)` / `R.FAIL()`)
- **Pagination**: `BasePageQuery` (page/size/sort) + `PageHelperUtils.pageQuery()` — results via `PageResponseUtils.buildPageResponse()`
- **Query objects**: Extend `BaseQuery` for search params (keyword, date range, filterSQL)
- **Auth**: JWT stored in Redis (`zs:user:login:{userId}`), 7-day expiry. Spring Security with `@PreAuthorize` for RBAC
- **RBAC**: Permissions like `user:list`, `user:add`, `customer:edit`, etc.
- **Dict conversion**: AOP-based via `@DictConvert` / `@EnableDictConversion` annotations
- **Data scope**: AOP via `@DataScope` for row-level data filtering
- **Mapper XML**: `src/main/resources/mapper/*.xml` — keep in sync with mapper interfaces

### Frontend (`crm-front/`)

- **Auto-imports**: Vue APIs, Pinia, Vue Router, and Element Plus components are auto-imported (no manual import needed). Generated files: `auto-imports.d.ts`, `components.d.ts`, `.eslintrc-auto-import.json`
- **HTTP client**: `src/core/http.js` — custom Axios wrapper with retry, loading, token injection, and error handling. Use `http.get/post/put/delete` methods.
- **API modules**: `src/api/` — one file per domain. Import via `import api from '@/api'` then call `api.user.getUserList()`
- **Store**: Pinia with persisted state (`pinia-plugin-persistedstate`). Stores in `src/store/modules/`
- **Vite alias**: `@` → `src/`, `~` → project root
- **SCSS variables**: Auto-injected globally from `src/assets/styles/variables.module.scss`
- **Dev proxy**: `/dev-api` → backend (default `http://101.43.158.81:8080`, rewrite strips prefix)
- **Routes**: Defined in `src/router/index.js`, lazy-loaded. Dashboard layout at `/dashboard` with nested child routes

## Code Style

### Backend

- Java 17, Spring Boot 3.4.2
- Lombok for boilerplate (`@Data`, `@Builder`, `@Resource`)
- `@Transactional(rollbackFor = Exception.class)` on write operations
- Inject dependencies with `@Resource` (not `@Autowired`)

### Frontend

- **Semicolons**: No semicolons in `.js`/`.ts`/`.vue` files (Prettier override)
- **Quotes**: Single quotes
- **Indent**: 2 spaces
- **Vue**: `<script>` and `<style>` blocks indented inside template (Prettier `vueIndentScriptAndStyle: true`)
- **Component names**: PascalCase in templates (`vue/component-name-in-template-casing`)
- **No trailing commas**

## Lint & Format Order

```bash
cd crm-front && npm run lint
```

Runs ESLint → Prettier → Stylelint in sequence with auto-fix.

## Commit Convention

Conventional Commits enforced via commitlint. Types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `revert`, `build`.

## Gotchas

- `application.yml` contains database/Redis credentials — do not commit real credentials to public repos
- Frontend dev proxy points to `101.43.158.81:8080` — change to `localhost:8080` for local backend
- Element Plus icons are manually registered in `main.js` (auto-import for icons not fully working)
- `crm-server` has no test files beyond the default Spring Boot test class
- The `@` path alias works in imports but the auto-import system sometimes generates duplicate configurations — check `auto-imports.d.ts` if you see type errors about missing globals
