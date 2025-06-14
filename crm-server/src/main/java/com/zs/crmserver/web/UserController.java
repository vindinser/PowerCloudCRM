package com.zs.crmserver.web;

import com.zs.crmserver.model.TUser;
import com.zs.crmserver.result.R;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/api/login/info")
  public R loginInfo(Authentication authentication) {
    TUser tUser = (TUser) authentication.getPrincipal();
    return R.OK(tUser);
  }
}
