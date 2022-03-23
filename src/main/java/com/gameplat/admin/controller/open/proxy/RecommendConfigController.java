package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.model.dto.RecommendConfigDto;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.RecommendConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.RecommendConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description : 代理配置 @Author : cc @Date : 2022/2/20
 */
@Api(tags = "代理配置")
@RestController
@RequestMapping("/api/admin/recommend/config")
public class RecommendConfigController {

  @Autowired private RecommendConfigService recommendConfigService;

  @GetMapping("/get")
  @ApiOperation(value = "获取层层代配置")
  public RecommendConfig getRecommendConfig() {
    return recommendConfigService.getRecommendConfig();
  }

  @GetMapping("/getLayerConfig")
  @ApiOperation(value = "获取层层代分红模式配置预设")
  public Map<String, List<GameDivideVo>> getLayerConfig() {
    return recommendConfigService.getDefaultLayerDivideConfig();
  }

  @GetMapping("/getFixConfig")
  @ApiOperation(value = "获取固定比例分红模式配置预设")
  public Map<String, List<GameDivideVo>> getFixConfig() {
    return recommendConfigService.getDefaultFixDivideConfig();
  }

  @GetMapping("/getFissionConfig")
  @ApiOperation(value = "获取裂变模式分红模式配置预设")
  public Map<String, Object> getFissionConfig() {
    return recommendConfigService.getDefaultFissionDivideConfig();
  }

  @PostMapping("/edit")
  @ApiOperation(value = "编辑层层代配置")
  @PreAuthorize("hasAuthority('recommendConfig:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑层层代配置")
  public void edit(@Validated @RequestBody RecommendConfigDto dto) {
    recommendConfigService.edit(dto);
  }
}
