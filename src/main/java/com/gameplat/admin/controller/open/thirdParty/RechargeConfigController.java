package com.gameplat.admin.controller.open.thirdParty;


import com.gameplat.admin.model.domain.RechargeConfig;
import com.gameplat.admin.service.RechargeConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/rechargeConfig")
public class RechargeConfigController {

  @Autowired private RechargeConfigService rechargeConfigService;


  @GetMapping("/queryAll")
  @PreAuthorize("hasAuthority('thirdParty:rechargeConfig:queryAll')")
  public List<RechargeConfig> queryAll(Integer memberLevel) {
    return rechargeConfigService.queryAll(memberLevel);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:rechargeConfig:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'新增充值限制payType=' + #rechargeConfig.payType")
  public void add(RechargeConfig rechargeConfig) {
    rechargeConfigService.add(rechargeConfig);
  }

}
