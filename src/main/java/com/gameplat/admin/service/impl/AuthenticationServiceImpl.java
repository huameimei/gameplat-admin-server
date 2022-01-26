package com.gameplat.admin.service.impl;

import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.GoogleAuthenticator;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private SysUserService userService;

  @Autowired private JwtTokenService jwtTokenService;

  @Autowired private PermissionService permissionService;

  @Autowired private PasswordService passwordService;

  @Override
  public UserToken login(AdminLoginDTO dto, HttpServletRequest request) {
    AdminLoginLimit limit =
        Assert.notNull(
            limitInfoService.getLimitInfo(LimitEnums.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class),
            "登录配置信息未配置");

    SysUser user = Assert.notNull(userService.getByUsername(dto.getAccount()), "用户不存在或密码不正确！");
    if (BooleanEnum.YES.match(limit.getGoogleAuthSwitch())) {
      this.checkGoogleAuthCode(user.getSafeCode(), dto.getGoogleCode());
    }

    String password = passwordService.decrypt(dto.getPassword());
    UserCredential credential = jwtTokenService.signIn(request, user.getUserName(), password);

    // 更新用户登录信息
    user.setLoginIp(IPUtils.getIpAddress(request));
    user.setLoginDate(new Date());
    Assert.isTrue(userService.updateById(user), () -> new ServiceException("登录失败!"));

    return UserToken.builder()
        .nickName(user.getNickName())
        .accessToken(credential.getAccessToken())
        .refreshToken(credential.getRefreshToken())
        .tokenExpireIn(credential.getTokenExpireIn())
        .clientType(credential.getClientType())
        .deviceType(credential.getDeviceType())
        .account(user.getUserName())
        .accessLogToken(permissionService.getAccessLogToken())
        .build();
  }

  @Override
  public RefreshToken refreshToken(String refreshToken) {
    UserCredential credential = jwtTokenService.refreshToken(refreshToken);
    return RefreshToken.builder()
        .refreshToken(credential.getRefreshToken())
        .accessToken(credential.getAccessToken())
        .tokenExpireIn(credential.getTokenExpireIn())
        .build();
  }

  @Override
  public void logout() {
    jwtTokenService.logout();
  }

  /**
   * 校验谷歌验证码是否正确
   *
   * @param safeCode String
   * @param googleCode String
   */
  private void checkGoogleAuthCode(String safeCode, String googleCode) {
    Assert.notBlank(safeCode, "安全码未设置");
    Assert.notBlank(googleCode, "安全码不能为空!");
    Assert.isTrue(GoogleAuthenticator.authcode(googleCode, safeCode), "安全码不正确!");
  }
}
