# AGENTS.md

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
