package com.gameplat.admin.service.game.api.ae.config;

import com.gameplat.admin.service.GameConfigService;
import com.gameplat.common.enums.GameCodeEnum;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AeConfig {

  @Resource
  private GameConfigService gameConfigService;

  public String getCert() {
    Map<String, String> configMap = gameConfigService
        .queryGameConfigInfoByPlatCode(GameCodeEnum.AE_LIVE.code());
    return configMap.getOrDefault("cert","VFd520gJgUT6oV21WCk");
  }

  public String getAgentId() {
    Map<String, String> configMap = gameConfigService
        .queryGameConfigInfoByPlatCode(GameCodeEnum.AE_LIVE.code());
    return configMap.getOrDefault("agent-id","kgsporta2");
  }

  public boolean isOpen() {
    Map<String, String> configMap = gameConfigService
        .queryGameConfigInfoByPlatCode(GameCodeEnum.AE_LIVE.code());
    return Boolean.parseBoolean(configMap.getOrDefault("open","true"));
  }

  public String getCurrency() {
    Map<String, String> configMap = gameConfigService
        .queryGameConfigInfoByPlatCode(GameCodeEnum.AE_LIVE.code());
    return configMap.getOrDefault("currency","CNY");
  }

  public String getPrefix() {
    Map<String, String> configMap = gameConfigService
        .queryGameConfigInfoByPlatCode(GameCodeEnum.AE_LIVE.code());
    return configMap.getOrDefault("prefix","kgsit");
  }

  public String getAccount(String account){
    return  account;
  }
}
