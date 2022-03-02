package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.model.entity.limit.LimitInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/system/limit")
public class OpenLimitInfoController {

  @Autowired private LimitInfoService limitInfoService;

  /** 添加/修改配置项 */
  @PutMapping(value = "/add")
  @PreAuthorize("hasAuthority('system:limit:add')")
  public void save(@RequestBody LimitInfoDTO limitInfoDTO) {
    limitInfoService.insertLimitInfo(limitInfoDTO);
  }

  /** 根据名称获取配置 */
  @GetMapping(value = "/get/{name}")
  @PreAuthorize("hasAuthority('system:limit:view')")
  public LimitInfo get(@PathVariable String name) {
    return limitInfoService.getLimitInfo(name);
  }
}
