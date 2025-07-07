package com.zs.crmserver.config.handler;

import com.zs.crmserver.result.CodeEnum;
import com.zs.crmserver.result.R;
import com.zs.crmserver.util.JSONUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 访问拒绝处理器
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException {
        R result = R.FAIL(CodeEnum.ACCESS_DENIED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONUtils.toJSON(result));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
