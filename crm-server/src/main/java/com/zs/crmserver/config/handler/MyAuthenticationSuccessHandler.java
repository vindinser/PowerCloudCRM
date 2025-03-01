package com.zs.crmserver.config.handler;

import com.zs.crmserver.model.TUser;
import com.zs.crmserver.result.R;
import com.zs.crmserver.util.JSONUtils;
import com.zs.crmserver.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功，执行该方法，在该方法中返回json给前端，就行了
        TUser tUser = (TUser) authentication.getPrincipal();

        //1、生成jwt
        //把tUser对象转成json作为负载数据放入jwt

        //2、写入redis

        //3、设置jwt的过期时间(如果选择了记住我，过期时间是7天，否则是30分钟)

        //登录成功的统一结果
        R result = R.OK(tUser);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
