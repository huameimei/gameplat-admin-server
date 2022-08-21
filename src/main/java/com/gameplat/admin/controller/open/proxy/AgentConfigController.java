package com.gameplat.admin.controller.open.proxy;

import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.service.AgentConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.AgentConfig;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "代理配置")
@RestController
@RequestMapping("/api/admin/agent/config")
public class AgentConfigController {

  @Autowired private AgentConfigService agentConfigService;

  @GetMapping("/get")
  @Operation(summary = "获取层层代配置")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理配置-->获取层层代配置:'")
  public AgentConfig getRecommendConfig() {
    return agentConfigService.getAgentConfig();
  }

  @GetMapping("/getLayerConfig")
  @Operation(summary = "获取层层代分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理配置-->获取层层代分红模式配置预设:'")
  public Map<String, List<GameDivideVo>> getLayerConfig() {
    return agentConfigService.getDefaultLayerDivideConfig();
  }

  @GetMapping("/getFixConfig")
  @Operation(summary = "获取固定比例分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public Map<String, List<GameDivideVo>> getFixConfig() {
    return agentConfigService.getDefaultFixDivideConfig();
  }

  @GetMapping("/getFissionConfig")
  @Operation(summary = "获取裂变模式分红模式配置预设")
  @PreAuthorize("hasAuthority('system:config:view:agent')")
  public Map<String, Object> getFissionConfig() {
    return agentConfigService.getDefaultFissionDivideConfig();
  }

  @PostMapping("/edit")
  @Operation(summary = "编辑代配置")
  @PreAuthorize("hasAuthority('system:config:update:agent')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'编辑层层代配置'")
  public void edit(@Validated @RequestBody Map<String, Object> params) {
    agentConfigService.edit(params);
  }
}
