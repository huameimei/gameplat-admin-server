package com.gameplat.admin.service;

import com.gameplat.common.compent.captcha.CaptchaProvider;
import com.gameplat.common.compent.captcha.image.Kaptcha;

import java.util.Map;

public interface CaptchaService {

  CaptchaProvider<Kaptcha> getImage();

  CaptchaProvider<Map<String, Object>> getGeetest();

  <T> CaptchaProvider<T> getCaptcha(String name);
}
