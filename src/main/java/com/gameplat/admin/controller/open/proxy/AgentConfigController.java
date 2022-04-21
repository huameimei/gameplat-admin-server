package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.AgentConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "代理配置")
@RestController
@RequestMapping("/api/admin/agent/config")
public class AgentConfigController {

  @Autowired private AgentConfigService agentConfigService;

  @GetMapping("/get")
  @ApiOperation(value = "获取层层代配置")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public AgentConfig getRecommendConfig() {
    return agentConfigService.getAgentConfig();
  }

  @GetMapping("/getLayerConfig")
  @ApiOperation(value = "获取层层代分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public Map<String, List<GameDivideVo>> getLayerConfig() {
    return agentConfigService.getDefaultLayerDivideConfig();
  }

  @GetMapping("/getFixConfig")
  @ApiOperation(value = "获取固定比例分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public Map<String, List<GameDivideVo>> getFixConfig() {
    return agentConfigService.getDefaultFixDivideConfig();
  }

  @GetMapping("/getFissionConfig")
  @ApiOperation(value = "获取裂变模式分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public Map<String, Object> getFissionConfig() {
    return agentConfigService.getDefaultFissionDivideConfig();
  }

  @PostMapping("/edit")
  @ApiOperation(value = "编辑代配置")
  @PreAuthorize("hasAuthority('system:config:update:agent')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "编辑层层代配置")
  public void edit(@Validated @RequestBody Map<String, Object> params) {
    agentConfigService.edit(params);
  }
}
