package com.gameplat.admin;

import com.gameplat.log.annotation.EnableLogging;
import com.gameplat.redis.annotation.EnableCaching;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableLogging
@EnableCaching(basePackages = "com.gameplat")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.gameplat.admin.**.dao")
@SpringBootApplication(scanBasePackages = "com.gameplat")
public class AdminServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminServiceApplication.class, args);
  }
}
