package com.zs.crmserver.config;

import com.zs.crmserver.config.filter.TokenVerifyFilter;
import com.zs.crmserver.config.handler.ExceptionHandlerProvider;
import com.zs.crmserver.config.handler.MyAuthenticationFailureHandler;
import com.zs.crmserver.config.handler.MyAuthenticationSuccessHandler;
import com.zs.crmserver.config.handler.MyLogoutSuccessHandler;
import com.zs.crmserver.constants.Constants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private TokenVerifyFilter tokenVerifyFilter;

    @Resource
    private ExceptionHandlerProvider exceptionHandlerProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChin(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
        // 禁用跨站请求伪造
        return httpSecurity
                   .formLogin((formLogin) -> {
                       formLogin.loginProcessingUrl(Constants.LOGIN_URI)
                           .usernameParameter("loginAct")
                           .passwordParameter("loginPwd")
                           .successHandler(myAuthenticationSuccessHandler)
                           .failureHandler(myAuthenticationFailureHandler);
                   })
                   .authorizeHttpRequests((authorizeHttpRequests) -> {
                       authorizeHttpRequests
                           .requestMatchers(Constants.LOGIN_URI).permitAll()
                           .anyRequest().authenticated(); // 任何请求都需要登录后访问
                   })
                   // .csrf((csrf) -> {
                   //   csrf.disable(); // 禁用跨站请求伪造
                   // })
                   .csrf(AbstractHttpConfigurer::disable) // 禁用跨站请求伪造

                   // 支持跨域
                   .cors((cors) -> {
                       cors.configurationSource(configurationSource);
                   })
                   .sessionManagement((sessionManagement) -> {
                       // session 创建策略
                       sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 无session状态，禁用session
                   })
                   // 添加自定义的 Filter
                   .addFilterBefore(tokenVerifyFilter, LogoutFilter.class)
                   // 异常处理器
                   .exceptionHandling((exceptionHandling) -> {
                       exceptionHandling
                           .authenticationEntryPoint(exceptionHandlerProvider.getAuthenticationEntryPoint())
                           .accessDeniedHandler(exceptionHandlerProvider.getAccessDeniedHandler());
                   })
                   
                   // 退出
                   .logout((logout) -> {
                       logout.logoutUrl("/api/logout") // 该地址不需要controller，由框架处理
                           .logoutSuccessHandler(myLogoutSuccessHandler);
                   })
                   .build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
