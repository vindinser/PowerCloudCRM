package com.zs.crmserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = { "com.zs.crmserver.mapper" })
@SpringBootApplication
public class CrmServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CrmServerApplication.class, args);
  }

}
