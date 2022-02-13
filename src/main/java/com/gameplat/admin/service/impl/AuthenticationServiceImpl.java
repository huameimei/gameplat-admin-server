package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.GoogleAuthCodeVO;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.RefreshToken;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.service.JwtTokenService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private LimitInfoService limitInfoService;

  @Autowired private SysUserService userService;

  @Autowired private JwtTokenService jwtTokenService;

  @Autowired private PermissionService permissionService;

  @Autowired private PasswordService passwordService;

  @Autowired private CodeVerifier codeVerifier;

  @Autowired private SecretGenerator secretGenerator;

  @Autowired private QrDataFactory qrDataFactory;

  @Autowired private QrGenerator qrGenerator;

  @Autowired private RedisTemplate<String, Integer> redisTemplate;

  private static final String KEY_2FA_RETRY_COUNT = "KEY_2FA_RETRY_COUNT";

  private static final int MAX_2FA_RETRY_COUNT = 3;

  @Override
  public UserToken login(AdminLoginDTO dto, HttpServletRequest request) {
    AdminLoginLimit limit = limitInfoService.getAdminLimit();
    // 是否启用了双因素认证
    boolean authenticated = EnableEnum.DISABLED.match(limit.getGoogleAuthSwitch());
    String password = passwordService.decrypt(dto.getPassword());
    UserCredential credential =
        jwtTokenService.login(dto.getAccount(), password, request, authenticated);

    // 更新用户登录信息
    this.updateLoginUserInfo(credential);
    String logAccessToken = permissionService.getAccessLogToken();

    return UserToken.builder()
        .nickName(credential.getNickname())
        .accessToken(credential.getAccessToken())
        .refreshToken(credential.getRefreshToken())
        .tokenExpireIn(credential.getTokenExpireIn())
        .clientType(credential.getClientType())
        .deviceType(credential.getDeviceType())
        .account(credential.getUsername())
        .accessLogToken(logAccessToken)
        .authenticated(authenticated)
        .isEnable2FA(credential.isEnable2FA())
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

  @Override
  public RefreshToken verify2FA(UserCredential credential, String code) {
    int retryCount =
        Optional.ofNullable(redisTemplate.opsForValue().get(KEY_2FA_RETRY_COUNT)).orElse(0);
    Assert.isTrue(retryCount < MAX_2FA_RETRY_COUNT, "安全码错误次数过多，请一小时后再试或联系客服！");

    String secret = userService.getSecret(credential.getUserId());
    if (!codeVerifier.isValidCode(secret, code)) {
      redisTemplate.opsForValue().set(KEY_2FA_RETRY_COUNT, ++retryCount, 1, TimeUnit.HOURS);
      throw new ServiceException("安全码错误，请重新输入!");
    }

    // 删除验证码错误次数
    redisTemplate.delete(KEY_2FA_RETRY_COUNT);

    // 颁发正式token
    return createAuthenticatedToken(credential);
  }

  @Override
  @SneakyThrows
  public GoogleAuthCodeVO create2FA(String username) {
    String secret = secretGenerator.generate();
    QrData data =
        qrDataFactory.newBuilder().label(username).secret(secret).issuer("AppName").build();

    String image =
        Utils.getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());

    return GoogleAuthCodeVO.builder().img(image).secret(secret).build();
  }

  @Override
  @SentinelResource(value = "bindSecret")
  public RefreshToken bindSecret(UserCredential credential, GoogleAuthDTO dto) {
    String secret = userService.getSecret(credential.getUserId());
    Assert.isTrue(StringUtils.isEmpty(secret), "您已经启用了两步验证，如需更换安全码，请联系客服!");

    // 验证
    Assert.isTrue(codeVerifier.isValidCode(dto.getSecret(), dto.getSafeCode()), "安全码错误，请重新输入!");

    // 绑定
    userService.bindSecret(credential.getUserId(), dto.getSecret());

    // 颁发正式token
    return createAuthenticatedToken(credential);
  }

  /**
   * 颁发正式token
   *
   * @param credential UserCredential
   * @return RefreshToken
   */
  private RefreshToken createAuthenticatedToken(UserCredential credential) {
    jwtTokenService.createToken(credential);
    return RefreshToken.builder()
        .refreshToken(credential.getRefreshToken())
        .accessToken(credential.getAccessToken())
        .tokenExpireIn(credential.getTokenExpireIn())
        .build();
  }

  private void updateLoginUserInfo(UserCredential credential) {
    SysUser user =
        SysUser.builder()
            .userId(credential.getUserId())
            .loginIp(credential.getLoginIp())
            .loginDate(credential.getLoginDate())
            .build();

    Assert.isTrue(userService.updateById(user), "登录失败，请联系客服！");
  }
}
