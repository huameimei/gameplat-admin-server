package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
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
  public LimitInfo<?> get(@PathVariable String name) {
    return limitInfoService.getLimitInfo(name);
  }
}
