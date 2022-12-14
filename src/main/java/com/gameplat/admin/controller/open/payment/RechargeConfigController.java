package com.gameplat.admin.controller.open.payment;

import com.gameplat.admin.service.RechargeConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.recharge.RechargeConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "充值配置")
@RestController
@RequestMapping("/api/admin/thirdParty/rechargeConfig")
public class RechargeConfigController {

  @Autowired private RechargeConfigService rechargeConfigService;

  @Operation(summary = "查询全部")
  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('thirdParty:rechargeConfig:view')")
  public List<RechargeConfig> queryAll(Integer mode, String payCode) {
    return rechargeConfigService.queryAll(-99, mode, payCode);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechargeConfig:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增充值限制payType=' + #rechargeConfig.payType")
  public void add(RechargeConfig rechargeConfig) {
    rechargeConfigService.add(rechargeConfig);
  }
}
