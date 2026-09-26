# crm-server CODING_STANDARDS.md

> 本文件定义后端项目的编码规范，所有后端代码（包括 AI 生成的代码）必须遵守。
>
> 基于阿里巴巴 Java 开发手册。

---

## 环境与工具链（mise）

> 所有代码（含 AI 生成的代码）必须在 **JDK 17** 下开发、编译与验证。

后端 JDK 由 [mise](https://mise.jdx.dev) 统一管理，版本清单在根目录 `mise.toml`（`java = "local-jdk17"`）。规则：

1. **JDK 锁定 17**。使用 `mise link` 引用本机已安装的 JDK，不重复下载：

   ```bash
   mise link java@local-jdk17 "D:/Utils/Java/jdk-17"
   ```

2. **构建 / 运行命令**（激活 shell 后可直接执行；未激活加 `mise exec --` 前缀）：

   ```bash
   mise exec -- ./mvnw -v              # 确认 Java version: 17.x
   mise exec -- ./mvnw compile         # 编译验证
   mise exec -- ./mvnw clean install   # 完整构建
   mise exec -- ./mvnw spring-boot:run # 启动服务
   ```

3. 本机存在多个 JDK（8/17/21/24）时，在各项目 `mise.toml` 中声明版本，**禁止再手动修改系统 `JAVA_HOME`**。
4. IDEA 中另行为项目配置 SDK 17（File → Project Structure → Project SDK），与 mise 互不影响。

---

## 一、环境要求

- JDK 17+
- IntelliJ IDEA（推荐）
- 项目已配置代码格式化模板，请导入项目设置

---

## 二、命名规范

### 2.1 通用命名

| 类型 | 规则 | 示例 |
|------|------|------|
| 类名 | UpperCamelCase | `UserController`, `CustomerService`, `BasePageQuery` |
| 方法名 | lowerCamelCase | `getUserById()`, `saveUser()`, `selectByPage()` |
| 常量 | UPPER_SNAKE_CASE | `LOGIN_URI`, `REDIS_JWT_KEY`, `MAX_UPLOAD_SIZE` |
| 变量 | lowerCamelCase | `loginUserId`, `userList`, `pageInfo` |
| 参数 | lowerCamelCase | `userId`, `query`, `keyword` |
| 包名 | 全小写 | `com.zs.crmserver.web` |

### 2.2 数据库命名

| 类型 | 规则 | 示例 |
|------|------|------|
| 表名 | `t_` 前缀 + 下划线 | `t_user`, `t_customer`, `t_activity_note` |
| 字段名 | 下划线 | `login_act`, `create_time`, `user_id` |
| 索引名 | `idx_` 前缀 + 下划线 | `idx_user_name`, `idx_customer_phone` |
| 唯一索引名 | `uk_` 前缀 + 下划线 | `uk_user_login_act` |

### 2.3 包结构规范

```
com.zs.crmserver/
├── web/                 # Controller 层
│   ├── system/          # 系统管理
│   ├── crm/             # CRM 业务
│   └── CommonController.java
├── service/             # Service 层
│   ├── system/
│   ├── crm/
│   └── impl/
├── mapper/              # MyBatis Mapper
│   ├── system/
│   ├── crm/
│   └── TUserMapper.java
├── model/               # 实体类
│   ├── system/
│   ├── crm/
│   └── BaseEntity.java
├── query/               # 查询参数
│   ├── BasePageQuery.java
│   ├── BaseQuery.java
│   └── CustomerQuery.java
├── result/              # 返回结果
│   ├── R.java
│   ├── PageResponse.java
│   └── LoginUser.java
├── config/              # 配置类
│   ├── SecurityConfig.java
│   ├── RedisConfig.java
│   └── SwaggerConfig.java
├── aspect/              # AOP 切面
│   ├── DictConvertAspect.java
│   └── DataScopeAspect.java
├── util/                # 工具类
│   ├── JwtUtils.java
│   ├── RedisUtils.java
│   └── PageHelperUtils.java
├── constants/           # 常量
│   ├── HttpStatus.java
│   └── DictType.java
└── exception/           # 异常处理
    ├── GlobalExceptionHandler.java
    └── BusinessException.java
```

---

## 三、分层规范

### 3.1 分层架构

```
Controller（web/）→ Service（service/）→ Mapper（mapper/）
```

### 3.2 各层职责

| 层级 | 职责 | 禁止 |
|------|------|------|
| **Controller** | 参数校验、调用 Service、返回结果 | 禁止写业务逻辑 |
| **Service** | 业务逻辑、事务管理 | 禁止直接操作数据库 |
| **Mapper** | SQL 操作、数据访问 | 禁止写业务逻辑 |

### 3.3 Controller 规范

```java
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('user:list')")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page")
    public R userPage(BasePageQuery query, @RequestParam(required = false) String keyword) {
        PageInfo<TUser> page = userService.getUserByPage(query, keyword);
        return PageResponseUtils.buildPageResponse(page);
    }
}
```

**规则：**
- 仅接收参数、调用 Service、返回结果
- 使用 `@PreAuthorize` 进行权限控制
- 统一返回 `R` 对象

### 3.4 Service 规范

```java
public interface UserService extends UserDetailsService {

    /**
     * 分页查询用户
     *
     * @param query   分页参数
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword);
}
```

```java
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword) {
        return PageHelperUtils.pageQuery(query, () -> userMapper.selectByPage(query, keyword));
    }
}
```

**规则：**
- 接口定义在 `service/` 包，实现类在 `service/impl/` 包
- 写操作必须添加 `@Transactional(rollbackFor = Exception.class)`
- 禁止在 Service 中直接操作数据库

### 3.5 Mapper 规范

```java
@Mapper
public interface UserMapper {

    /**
     * 分页查询用户
     *
     * @param query   分页参数
     * @param keyword 搜索关键词
     * @return 用户列表
     */
    List<TUser> selectByPage(BasePageQuery query, String keyword);
}
```

```xml
<!-- src/main/resources/mapper/UserMapper.xml -->
<select id="selectByPage" resultType="TUser">
    SELECT
        id,
        login_act,
        name,
        phone,
        email,
        create_time
    FROM t_user
    <where>
        <if test="keyword != null and keyword != ''">
            AND (name LIKE CONCAT('%', #{keyword}, '%')
                 OR phone LIKE CONCAT('%', #{keyword}, '%'))
        </if>
    </where>
    ORDER BY create_time DESC
</select>
```

**规则：**
- Mapper 接口方法名与 XML 中 `id` 保持一致
- 使用 `#{param}` 防止 SQL 注入
- 禁止使用 `${}` 拼接 SQL（除非 `filterSQL` 等动态条件）

---

## 四、注解使用

| 场景 | 注解 | 说明 |
|------|------|------|
| 依赖注入 | `@Resource` | 不用 `@Autowired` |
| 事务控制 | `@Transactional(rollbackFor = Exception.class)` | 写操作必须添加 |
| 权限控制 | `@PreAuthorize("hasAuthority('xxx')")` | 接口级权限 |
| 参数校验 | `@Valid` + `@NotNull` / `@NotBlank` | 请求参数校验 |
| 字典转换 | `@DictConvert` | 字段级字典转换 |
| 数据权限 | `@DataScope` | 行级数据过滤 |

---

## 五、响应格式

### 5.1 统一响应对象

```java
// 成功（无数据）
return R.OK();

// 成功（有数据）
return R.OK(data);

// 分页数据
return PageResponseUtils.buildPageResponse(pageInfo);

// 失败
return R.FAIL();

// 失败（自定义消息）
return R.FAIL("用户不存在");
```

### 5.2 响应结构

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

### 5.3 分页响应

```java
return PageResponseUtils.buildPageResponse(pageInfo);
```

返回格式：
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

## 六、分页查询

### 6.1 标准写法

```java
/**
 * 分页查询用户列表
 *
 * @param query   分页参数（page/size/sort）
 * @param keyword 搜索关键词
 * @return 分页结果
 */
public PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword) {
    return PageHelperUtils.pageQuery(query, () -> userMapper.selectByPage(query, keyword));
}
```

### 6.2 查询参数类

```java
@Data
public class CustomerQuery extends BaseQuery {

    /** 客户名称 */
    private String name;

    /** 客户来源 */
    private String source;

    /** 客户级别 */
    private String level;

    /** 负责人ID */
    private Integer ownerId;
}
```

---

## 七、JavaDoc 注释规范

### 7.1 Controller

```java
/**
 * 用户管理控制器
 *
 * @author zs
 * @since 1.0.0
 */
@RestController
public class UserController {

    /**
     * 分页查询用户列表
     *
     * @param query   分页参数（page/size/sort）
     * @param keyword 搜索关键词（可选）
     * @return 分页用户列表
     */
    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping("/api/users")
    public R userPage(
        BasePageQuery query,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        PageInfo<TUser> userList = userService.getUserByPage(query, keyword);
        return PageResponseUtils.buildPageResponse(userList);
    }
}
```

### 7.2 Service 接口

```java
/**
 * 用户服务接口
 *
 * @author zs
 * @since 1.0.0
 */
public interface UserService extends UserDetailsService {

    /**
     * 分页查询用户
     *
     * @param query   分页参数
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageInfo<TUser> getUserByPage(BasePageQuery query, String keyword);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    TUser getUserById(Integer id);
}
```

### 7.3 实体类

```java
/**
 * 用户表实体类
 *
 * @author zs
 * @since 1.0.0
 */
@Data
public class TUser implements UserDetails, Serializable {

    /**
     * 主键，自动增长
     */
    private Integer id;

    /**
     * 登录账号
     */
    private String loginAct;

    /**
     * 登录密码（BCrypt加密）
     */
    private String loginPwd;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态（0 禁用，1 启用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
```

### 7.4 自定义注解

```java
/**
 * 字段级注解：标记需要字典转换的字段
 *
 * <p>在实体类字段上添加此注解，指定字典类型和目标字段</p>
 *
 * @author zs
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictConvert {

    /**
     * 字典类型（缓存中的 key）
     *
     * @return 字典类型标识
     */
    String dicType();

    /**
     * 目标字段名（转换后的名称字段）
     *
     * @return 目标字段名
     */
    String targetField();
}
```

---

## 八、MyBatis XML 规范

### 8.1 SQL 规范

```xml
<!-- 用户分页查询 -->
<select id="selectByPage" resultType="TUser">
    SELECT
        id,
        login_act,
        name,
        phone,
        email,
        create_time
    FROM t_user
    <where>
        <if test="keyword != null and keyword != ''">
            AND (name LIKE CONCAT('%', #{keyword}, '%')
                 OR phone LIKE CONCAT('%', #{keyword}, '%'))
        </if>
        <if test="filterSQL != null and filterSQL != ''">
            ${filterSQL}
        </if>
    </where>
    ORDER BY create_time DESC
</select>
```

### 8.2 规则

- 使用 `<where>` 标签自动处理 `WHERE` 关键字和 `AND`
- 使用 `<if>` 标签动态拼接条件
- 使用 `#{param}` 防止 SQL 注入
- 仅 `filterSQL` 等动态条件使用 `${}`
- 字段名使用下划线命名（与数据库一致）
- 实体类属性使用驼峰命名

---

## 九、异常处理

### 9.1 全局异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return R.FAIL(e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 参数校验异常
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return R.FAIL(message);
    }
}
```

### 9.2 业务异常

```java
/**
 * 业务异常
 *
 * @author zs
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
```

**使用方式：**
```java
throw new BusinessException("用户不存在");
```

---

## 十、安全规范

### 10.1 密码加密

```java
// 密码加密
String encodedPassword = passwordEncoder.encode(rawPassword);

// 密码匹配
boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
```

### 10.2 SQL 注入防护

- 使用 `#{param}` 预编译参数
- 禁止拼接 SQL 字符串
- 仅 `filterSQL` 等动态条件使用 `${}`（需谨慎）

### 10.3 敏感信息保护

- 禁止在日志中打印密码、密钥
- 禁止将 `.env`、`application.yml` 等配置文件提交
- 使用环境变量或配置中心管理敏感信息

---

## 十一、Git 提交规范

### 11.1 Commit Message 格式

```
<type>(<scope>): <subject>
```

### 11.2 type 类型

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

### 11.3 scope 可选值

`user`, `customer`, `clue`, `activity`, `statistic`, `auth`, `common`

### 11.4 示例

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

## 十二、AI 生成代码规范

### 12.1 生成前检查

生成后端代码前，AI 必须确认：

- [ ] 使用 `@Resource` 注入依赖
- [ ] 写操作添加 `@Transactional(rollbackFor = Exception.class)`
- [ ] 接口方法有 JavaDoc 注释
- [ ] 返回 `R` 对象
- [ ] 分页使用 `PageHelperUtils.pageQuery()`
- [ ] 权限注解 `@PreAuthorize`
- [ ] 实体类字段有注释
- [ ] Mapper XML 与接口方法名一致
- [ ] 项目通过编译 `./mvnw clean install`

### 12.2 代码审查要点

AI 生成的代码需重点审查：
- 是否遵循项目现有代码风格
- 是否使用项目约定的工具和组件
- 是否存在安全漏洞（SQL 注入、敏感信息暴露等）
- 是否添加了必要的注释和文档
- 是否通过了自动化检查（Maven 编译）

---

## 十三、常用工具类

### 13.1 分页工具

```java
PageHelperUtils.pageQuery(query, () -> mapper.selectByPage(query, keyword));
```

### 13.2 Redis 工具

```java
redisUtils.set(key, value, timeout, TimeUnit.MINUTES);
Object value = redisUtils.get(key);
```

### 13.3 JWT 工具

```java
String token = JwtUtils.generateToken(loginUser);
LoginUser loginUser = JwtUtils.parseToken(token);
```

---

## 十四、提交前检查清单

- [ ] 代码通过编译：`./mvnw clean install`
- [ ] 无编译警告
- [ ] 接口方法有 JavaDoc 注释
- [ ] 写操作有 `@Transactional`
- [ ] 返回 `R` 对象
- [ ] 无敏感信息（密码、密钥）
- [ ] 提交信息符合 Conventional Commits 规范

---

## 十五、更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-01-01 | 初始版本，基于阿里巴巴 Java 开发手册 |
