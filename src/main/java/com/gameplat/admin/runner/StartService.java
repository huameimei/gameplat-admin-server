package com.gameplat.admin.runner;

import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** 继承 ApplicationRunner 接口后项目启动时会按照执行顺序执行 run 方法 */
@Slf4j
@Component
@Order(value = 1)
@RequiredArgsConstructor
public class StartService implements ApplicationRunner {

  private static Logger logger = LoggerFactory.getLogger(StartService.class);

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private SysDictTypeService sysDictTypeService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // TODO
  }
}
