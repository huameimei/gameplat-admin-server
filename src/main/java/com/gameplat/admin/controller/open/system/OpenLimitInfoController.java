package com.gameplat.admin.controller.open.system;

import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    if (StringUtils.isBlank(limitInfoDTO.getName())) {
      throw new ServiceException("名称不能为空");
    }
    if (Objects.isNull(limitInfoDTO.getParams())) {
      throw new ServiceException("数据不能为空");
    }
    limitInfoService.insertLimitInfo(limitInfoDTO);
  }

  /** 根据名称获取配置 */
  @GetMapping(value = "/get/{name}")
  @PreAuthorize("hasAuthority('system:limit:view')")
  public LimitInfo<?> get(@PathVariable String name) {
    return limitInfoService.getLimitInfo(name);
  }
}
