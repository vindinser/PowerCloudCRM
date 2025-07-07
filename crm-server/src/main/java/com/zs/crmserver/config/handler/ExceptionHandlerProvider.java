package com.zs.crmserver.config.handler;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 异常处理器提供器
 */
@Component
public class ExceptionHandlerProvider {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public ExceptionHandlerProvider(CustomAuthenticationEntryPoint authenticationEntryPoint,
                                    CustomAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    public AccessDeniedHandler getAccessDeniedHandler() {
        return accessDeniedHandler;
    }
}
