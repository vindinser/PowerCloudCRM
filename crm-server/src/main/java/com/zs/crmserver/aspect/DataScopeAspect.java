package com.zs.crmserver.aspect;

import com.zs.crmserver.commons.DataScope;
import com.zs.crmserver.constants.Constants;
import com.zs.crmserver.model.TUser;
import com.zs.crmserver.query.BaseQuery;
import com.zs.crmserver.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * 拦截器类
 * 拦截DataScope注解
 */
@Aspect
@Component
public class DataScopeAspect {

    // 切入点
    @Pointcut(value = "@annotation(com.zs.crmserver.commons.DataScope)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取签名
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        // 获取注解
        DataScope dataScope = methodSignature.getMethod().getDeclaredAnnotation(DataScope.class);

        String tableAlias = dataScope.tableAlias();
        String tableField = dataScope.tableField();

        // 再 spring web 容器中，获取到当前请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 请求对象中获取token
        String token = request.getHeader(Constants.TOKEN_NAME);
        // 从 token 中解析当前用户是管理员还是用户
        TUser tUser = JWTUtils.parseUserFromJWT(token);
        List<String> roleList = tUser.getRoleList();

        // 普通用户，查自己（拼接过滤搜索条件）； admin，查所有，无需拼接过滤搜索条件
        if(!roleList.contains("admin")){
            // 获取请求参数（AOP规定过滤参数必须写在第一个，否则会失效）
            Object params = joinPoint.getArgs()[0];
            if(params instanceof BaseQuery) {
                BaseQuery baseQuery = (BaseQuery)params;
                baseQuery.setFilterSQL(" and " + tableAlias + "." + tableField + "=" + tUser.getId());
            }
        }

        Object proceed = joinPoint.proceed();

        // 如果需要可以在执行后继续操作

        return proceed;
    }
}
