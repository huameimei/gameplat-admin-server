package com.gameplat.admin.service.game.api.ae.config;

import com.gameplat.admin.component.LiveConfigEnvCache;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AeConfig extends Properties {

  public String getHost() {
//    return this.getProperty("ae.host");
    return "https://tttint.onlinegames22.com";
  }


  public String getCert() {
//    return this.getProperty("ae.cert");
    return "VFd520gJgUT6oV21WCk";
  }

  public String getAgentId() {
//    return this.getProperty("ae.agent-id");
    return "kgsporta2";
  }

  public boolean isOpen() {
//    return Boolean.parseBoolean(this.getProperty("ae.open"));
    return true;
  }

  public String getCurrency() {
//    return this.getProperty("ae.currency");
    return "CNY";
  }

  public String getPrefix() {
//    return EnvContextManager.getProsByName("fg.prefix");
    return "kgsit";
  }

  public String getAccount(String account){
    return  account;
  }
}
