package com.gameplat.admin.service.impl;

import com.gameplat.admin.service.CaptchaService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.common.compent.captcha.CaptchaConfig;
import com.gameplat.common.compent.captcha.CaptchaEnums;
import com.gameplat.common.compent.captcha.CaptchaProvider;
import com.gameplat.common.compent.captcha.CaptchaStrategyContext;
import com.gameplat.common.compent.captcha.image.Kaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.gameplat.common.enums.DictTypeEnum.VERIFICATION_CODE_CONFIG;

@Service
public class CaptchaServiceImpl implements CaptchaService {

  @Autowired private ConfigService configService;

  @Autowired private CaptchaStrategyContext captchaStrategyContext;

  @Override
  public <T> CaptchaProvider<T> getCaptcha(String name) {
    CaptchaEnums enums = CaptchaEnums.getByName(name);
    CaptchaConfig config = this.getConfig(enums);
    return captchaStrategyContext.getProvider(enums.getProvider(), config);
  }

  @Override
  public CaptchaProvider<Kaptcha> getImage() {
    return this.getCaptcha(CaptchaEnums.IMAGE.getName());
  }

  @Override
  public CaptchaProvider<Map<String, Object>> getGeetest() {
    return this.getCaptcha(CaptchaEnums.GEETEST.getName());
  }

  private CaptchaConfig getConfig(CaptchaEnums enums) {
    return configService.get(
        VERIFICATION_CODE_CONFIG.getValue(), enums.getName(), enums.getConfig());
  }
}
