package com.gameplat.admin.controller.open.lottery;


import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.gameplat.admin.config.LotteryConfig;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.web.Result;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/lottery")
@Slf4j
public class LotteryController {

  @Resource
  private RoutingDelegate routingDelegate;

  @Resource
  private LotteryConfig lotteryConfig;


  @RequestMapping(value = "/api-manage/**", method = {RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT})
  public Result<?> getOrder(HttpServletRequest request, HttpServletResponse response) {
    String uri = request.getRequestURI();
    String platformCode = lotteryConfig.getPlatformCode();
    String proxyCode = lotteryConfig.getProxyCode();
    if (StringUtils.isAnyBlank(platformCode, proxyCode)) {
      log.info("彩票服转发前获取租户标识失败 platformCode={}, proxyCode={}", platformCode, proxyCode);
      throw new RuntimeException("彩票服转发前获取租户标识失败 platformCode=" + platformCode + "proxyCode=" + proxyCode);
    }
    String attribution = platformCode + ":" + proxyCode;
    //获取登录信息
    UserCredential userCredential = SecurityUserHolder.getCredential();
    if (userCredential == null) {
      throw new RuntimeException("彩票服转发前获取登录用户信息失败");
    }
    log.info("彩票服转发请求参数: url={}, attribution={}", uri, attribution);

    //封装请求头参数
    Map<String, String> headers = new HashMap<>();
    headers.put("locale", "zh");
    headers.put("attribution", attribution);
    headers.put("username", userCredential.getUsername());
    Object object = routingDelegate.redirect(request,headers,lotteryConfig.getServerHost(),"/api/admin/lottery");
    JSONObject jsonObject = JSONUtil.parseObj(JSONUtil.toJsonStr(object));
    JSONObject bodyObject = JSONUtil.parseObj(jsonObject.getStr("body"));
    log.info("彩票服务转发请求响应数据：{}",JSONUtil.toJsonStr(object));
    if (bodyObject.getInt("code") == HttpStatus.HTTP_OK) {
      return Result.succeedData(bodyObject.getObj("data"));
    }else{
      return Result.failed(bodyObject.getStr("msg"));
    }
  }
}
