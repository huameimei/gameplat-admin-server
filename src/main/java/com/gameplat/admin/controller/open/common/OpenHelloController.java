package com.gameplat.admin.controller.open.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class OpenHelloController {

  @GetMapping("/hello")
  public String index() {
    log.info("请求接口:[/api/admin/hello]");
    return "Hello!";
  }
}
