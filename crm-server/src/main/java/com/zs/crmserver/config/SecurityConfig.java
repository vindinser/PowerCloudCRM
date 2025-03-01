package com.zs.crmserver.config;

import com.zs.crmserver.config.handler.MyAuthenticationFailureHandler;
import com.zs.crmserver.config.handler.MyAuthenticationSuccessHandler;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
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

  @Bean
  public SecurityFilterChain securityFilterChin(HttpSecurity httpSecurity, CorsConfigurationSource configurationSource) throws Exception {
    return httpSecurity
     .formLogin((formLogin) -> {
       formLogin.loginProcessingUrl("/api/login")
         .usernameParameter("loginAct")
         .passwordParameter("loginPwd")
         .successHandler(myAuthenticationSuccessHandler)
         .failureHandler(myAuthenticationFailureHandler);
     })
     .authorizeHttpRequests((authorizeHttpRequests) -> {
       authorizeHttpRequests.anyRequest().authenticated(); // 任何请求都需要登录后访问
     })

     // 支持跨域
     .cors((cors) -> {
       cors.configurationSource(configurationSource);
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
