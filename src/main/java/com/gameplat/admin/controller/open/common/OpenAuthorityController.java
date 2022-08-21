package com.gameplat.admin.controller.open.common;

import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.GoogleAuthCodeVO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.admin.service.AuthenticationService;
import com.gameplat.admin.service.TwoFactorAuthenticationService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.annotation.LoginLog;
import com.gameplat.log.enums.LogType;
import com.gameplat.security.context.UserCredential;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * 用户授权管理控制类
 *
 * @author three
 */
@Validated
@Tag(name = "登录")
@RestController
@RequestMapping("/api/admin/auth")
public class OpenAuthorityController {

  @Autowired private AuthenticationService authenticationService;

  @Autowired private TwoFactorAuthenticationService twoFactorAuthenticationService;

  @Operation(summary = "登录")
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  @LoginLog(module = ServiceName.ADMIN_SERVICE, desc = "'账号'+#dto.account+'登录系统'")
  public UserToken login(@Validated AdminLoginDTO dto, HttpServletRequest request) {
    return authenticationService.login(dto, request);
  }

  @Operation(summary = "账号登出")
  @PostMapping("/logout")
  @LoginLog(isLogout = true, module = ServiceName.ADMIN_SERVICE, desc = "'账号登出系统'")
  public void logout() {
    authenticationService.logout();
  }

  @Operation(summary = "刷新TOKEN")
  @PostMapping("/refreshToken")
  @Log(module = ServiceName.ADMIN_SERVICE, desc = "'刷新token'")
  public RefreshToken refreshToken(@RequestParam String refreshToken) {
    return authenticationService.refreshToken(refreshToken);
  }

  @Operation(summary = "两步认证")
  @PostMapping("/verify2fa")
  @PreAuthorize("hasRole('ROLE_2FA_VERIFICATION_USER')")
  public RefreshToken verifyCode(
      @AuthenticationPrincipal UserCredential credential,
      @NotEmpty(message = "请输入安全码") String code) {
    return authenticationService.verify2Fa(credential, code);
  }

  @SneakyThrows
  @Operation(summary = "获取谷歌认证码")
  @GetMapping(value = "/authCode")
  public GoogleAuthCodeVO getAuthCode(@AuthenticationPrincipal UserCredential credential) {
    return twoFactorAuthenticationService.create2Fa(credential.getUsername());
  }

  @Operation(summary = "绑定谷歌密钥")
  @PostMapping("/bindSecret")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'登录->绑定谷歌密钥:' + #dto")
  public void bindSecret(@Validated GoogleAuthDTO dto) {
    twoFactorAuthenticationService.bindSecret(dto);
  }
}
