package com.gameplat.admin.controller.open.common;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.model.bean.Language;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.admin.service.SysCommonService;
import com.gameplat.common.captcha.CaptchaImage;
import com.gameplat.common.security.SecurityUserHolder;
import com.gameplat.common.util.StringUtils;
import com.gameplat.web.captcha.CaptchaProducer;
import com.gameplat.web.idempoten.AutoIdempotent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公共控制类
 *
 * @author three
 */
@Slf4j
@Controller
@RequestMapping("/api/admin/common")
public class OpenCommonController {

  @Autowired private SysCommonService commonService;

  @Autowired private PermissionService permissionService;

  @Autowired private CaptchaProducer producer;

  /**
   * 获取验证码
   *
   * @throws IOException IOException
   */
  @GetMapping("/captcha/{deviceId}")
  public CaptchaImage createCode(@PathVariable String deviceId, HttpServletResponse response)
      throws IOException {
    Assert.notNull(deviceId, "机器码不能为空");
    response.setHeader("param", "no-cache");
    response.setHeader("cache-control", "no-cache");
    response.setIntHeader("expires", 0);
    return producer.createCaptcha(deviceId);
  }

  /**
   * 系统菜单列表
   *
   * @return ArrayList
   */
  @ResponseBody
  @GetMapping("/menuList")
  @AutoIdempotent(key = "menuList", expir = 1000L)
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  /** 获取字典数据 */
  @ResponseBody
  @GetMapping("/getDictByTypes/{types}")
  @AutoIdempotent(key = "getDictByTypes", expir = 2000L)
  public Map<Object, List<JSONObject>> getDictByTypes(@PathVariable String types) {
    if (StringUtils.isEmpty(types)) {
      return null;
    }
    return commonService.getDictByTypes(types);
  }

  @ResponseBody
  @GetMapping("/language")
  @AutoIdempotent(key = "language", expir = 2000L)
  public List<Language> getLanguage() {
    return commonService.language();
  }

  /**
   * 系统授权信息
   *
   * @return AdminLoginLimit
   */
  @ResponseBody
  @GetMapping("/checkAuth")
  @AutoIdempotent(key = "language", expir = 2000L)
  public AdminLoginLimit checkAuth() {
    return commonService.checkAuth();
  }
}
