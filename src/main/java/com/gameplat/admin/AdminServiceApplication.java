package com.gameplat.admin;

import com.gameplat.log.annotation.EnableLogging;
import com.gameplat.redis.annotation.EnableCaching;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

@EnableLogging
@EnableCaching(basePackages = "com.gameplat")
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.gameplat.admin.**.dao")
@SpringBootApplication(scanBasePackages = "com.gameplat")
public class AdminServiceApplication implements ApplicationListener<WebServerInitializedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        WebServerApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        int port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        System.out.println(String.format("\n\n------------------------  AdminService is Running,the port %s ,the contextPath %s  --------------------\n\n", port, contextPath));
    }
}
