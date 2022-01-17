package com.gameplat.admin.controller.open.common;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.admin.service.AuthenticationService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.util.GoogleAuthenticator;
import com.gameplat.base.common.util.ServletUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.annotation.LoginLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户授权管理控制类
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/auth")
public class OpenAuthorityController {

  @Autowired private SysUserService userService;

  @Autowired private AuthenticationService authenticationService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @LoginLog(module = ServiceName.ADMIN_SERVICE, desc = "'账号'+#dto.account+'登录系统'")
  public UserToken login(@Validated @RequestBody AdminLoginDTO dto, HttpServletRequest request) {
    return authenticationService.login(dto, request);
  }

  /** 账号登出 */
  @GetMapping("/logout")
  @LoginLog(isLogout = true, module = ServiceName.ADMIN_SERVICE, desc = "账号登出系统")
  public void logout() {
    authenticationService.logout();
  }

  /** 刷新token */
  @PostMapping("/refreshToken")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "刷新token")
  public RefreshToken refreshToken(@RequestParam String refreshToken) {
    return authenticationService.refreshToken(refreshToken);
  }

  /**
   * 获取二维码路径
   *
   * @param userName String
   * @return JSONObject
   */
  @GetMapping(value = "/authCode")
  public JSONObject getAuthCode(@RequestParam String userName, HttpServletRequest request) {
    String cDomain = ServletUtils.getBaseUrl(request);
    // 生成密钥
    String secret = GoogleAuthenticator.genSecret(userName, cDomain);
    String url = GoogleAuthenticator.getQRBarcodeURL(userName, "KgSport", secret);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("url", url);
    jsonObject.put("secret", secret);
    return jsonObject;
  }

  /**
   * 为用户绑定谷歌密钥
   *
   * @param dto GoogleAuthDTO
   */
  @PostMapping("bindAuth")
  public void bindSecret(@Validated GoogleAuthDTO dto) {
    userService.bindSecret(dto);
  }
}
