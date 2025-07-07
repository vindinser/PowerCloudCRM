package com.zs.crmserver.config.handler;

import com.zs.crmserver.result.CodeEnum;
import com.zs.crmserver.result.R;
import com.zs.crmserver.util.JSONUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证入口点处理器
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        R result = R.FAIL(CodeEnum.TOKEN_IS_EMPTY);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONUtils.toJSON(result));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
