package com.gameplat.admin.controller.open.sport;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.LiveDomainParamDTO;
import com.gameplat.admin.service.ILiveDomainService;
import com.gameplat.base.common.web.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "直播域名")
@Slf4j
@RestController
@RequestMapping("/api/admin/sport/live")
public class LiveDomainTrafficController {

  @Autowired private ILiveDomainService liveDomainService;

  @Operation(summary = "直播域名查询")
  @GetMapping("/getLiveDomainList")
  public Result<Object> getLiveDomainList(
      LiveDomainParamDTO param,
      @RequestHeader(value = "country", required = false, defaultValue = "zh-CN") String country) {
    param.setCountry(country);
    return Result.succeedData(liveDomainService.getLiveDomainList(param));
  }

  @Operation(summary = "直播流量查询")
  @GetMapping("/getLiveDomainTrafficData")
  public Result<Object> getLiveDomainTrafficData(
      LiveDomainParamDTO param,
      @RequestHeader(value = "country", required = false, defaultValue = "zh-CN") String country) {
    param.setCountry(country);
    String code = Convert.toStr(liveDomainService.getLiveDomainTrafficData(param));
    if (StringUtils.isNotBlank(code)) {
      JSONObject jsonObject = JSONObject.parseObject(code);
      return Result.succeedData(jsonObject);
    }
    return Result.failed("接口调用失败,校验参数后请重新请求！");
  }
}
