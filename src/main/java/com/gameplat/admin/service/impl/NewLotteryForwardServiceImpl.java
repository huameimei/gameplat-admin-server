package com.gameplat.admin.service.impl;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.NewLotteryForwardService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.web.Result;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NewLotteryForwardServiceImpl implements NewLotteryForwardService {

  @Autowired private RoutingDelegate routingDelegate;

  @Autowired
  private GameConfigService gameConfigService;

  @Override
  public Object serviceHandler(HttpServletRequest request) {
    String uri = request.getRequestURI();
    JSONObject gameConfig = gameConfigService.getGameConfig(GamePlatformEnum.KGNL.getCode());
    log.info("彩票配置：{}", JSON.toJSONString(gameConfig));
    String proxyCode = gameConfig.getString("proxy");
    String platformCode = gameConfig.getString("platform");

    if (StringUtils.isAnyBlank(platformCode, proxyCode)) {
      log.info("彩票服转发前获取租户标识失败 platformCode={}, proxyCode={}", platformCode, proxyCode);
      throw new RuntimeException(
          "彩票服转发前获取租户标识失败 platformCode=" + platformCode + "proxyCode=" + proxyCode);
    }
    String attribution = platformCode + ":" + proxyCode;
    // 获取登录信息
    UserCredential userCredential = SecurityUserHolder.getCredential();
    if (userCredential == null) {
      throw new RuntimeException("彩票服转发前获取登录用户信息失败");
    }
    log.info("彩票服转发请求参数: url={}, attribution={}", uri, attribution);

    // 封装请求头参数
    Map<String, String> headers = new HashMap<>();
    headers.put("locale", "zh");
    headers.put("attribution", attribution);
    headers.put("username", userCredential.getUsername());
    Object object =
        routingDelegate.redirect(
                request, headers, gameConfig.getString("host"), "/api/admin/lottery");
    String result = JSONUtil.toJsonStr(object);
    com.alibaba.fastjson.JSONObject jsonObject1 =
        com.alibaba.fastjson.JSONObject.parseObject(result);
    String body = jsonObject1.getString("body");
    com.alibaba.fastjson.JSONObject bodyObject = com.alibaba.fastjson.JSONObject.parseObject(body);
    log.info("彩票服务转发请求响应数据：{}", JSONUtil.toJsonStr(object));
    if (bodyObject.getInteger("code") == HttpStatus.HTTP_OK) {
      return Result.succeedData(bodyObject.get("data"));
    } else {
      return Result.failed(bodyObject.getString("msg"));
    }
  }
}
