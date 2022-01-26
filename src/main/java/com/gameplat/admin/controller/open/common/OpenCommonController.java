package com.gameplat.admin.controller.open.common;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.bean.router.VueRouter;
import com.gameplat.admin.model.domain.SysMenu;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.PermissionService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.compent.captcha.CaptchaConfig;
import com.gameplat.common.compent.captcha.CaptchaEnums;
import com.gameplat.common.compent.captcha.CaptchaProvider;
import com.gameplat.common.compent.captcha.CaptchaStrategyContext;
import com.gameplat.common.compent.captcha.image.Kaptcha;
import com.gameplat.security.SecurityUserHolder;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 公共控制类
 *
 * @author three
 */
@Api(tags = "公共控制类")
@Slf4j
@RestController
@RequestMapping("/api/admin/common")
public class OpenCommonController {

  @Autowired private CommonService commonService;

  @Autowired private PermissionService permissionService;

  @Autowired private CaptchaStrategyContext captchaStrategyContext;

  @GetMapping("/vCode")
  public Kaptcha createCode(HttpServletRequest request, HttpServletResponse response) {
    response.setHeader("param", "no-cache");
    response.setHeader("cache-control", "no-cache");
    response.setIntHeader("expires", 0);
    return captchaStrategyContext.getImage().create(request, 1);
  }

  @GetMapping("/menuList")
  public ArrayList<VueRouter<SysMenu>> menuList() {
    return permissionService.getMenuList(SecurityUserHolder.getUsername());
  }

  @ApiOperation(value = "获取字典数据")
  @GetMapping("/getDictByTypes/{types}")
  public Map<Object, List<JSONObject>> getDictByTypes(@PathVariable String types) {
    return StringUtils.isEmpty(types) ? null : commonService.getDictByTypes(types);
  }

  @GetMapping("/config")
  public ConfigVO config() {
    return commonService.getConfig();
  }
}
