package com.gameplat.admin.service.impl;

import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.constant.RsaConstant;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.enums.ErrorPasswordLimit;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.AdminLoginDTO;
import com.gameplat.admin.model.vo.RefreshToken;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.admin.service.AuthenticationService;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.common.enums.SystemCodeType;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.ip.IpAddressParser;
import com.gameplat.common.util.GoogleAuthenticator;
import com.gameplat.common.util.RSAUtils;
import com.gameplat.common.util.StringUtils;
import com.gameplat.redis.captcha.CaptchaProducer;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.service.JwtTokenService;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private SysUserService userService;

  @Autowired private SysAuthIpService authIpService;

  @Autowired private CaptchaProducer captchaProducer;

  @Autowired private AdminCache adminCache;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtTokenService jwtTokenService;

  @Override
  public UserToken login(AdminLoginDTO dto, HttpServletRequest request) {
    // 获取管理员登录限制信息
    AdminLoginLimit limit =
        dictDataService.getDictData(DictTypeEnum.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class);
    if (null == limit) {
      throw new ServiceException("登录配置信息未配置");
    }

    // 是否开启后台白名单
    String requestIp = IpAddressParser.getClientIp(request);
    if (SystemCodeType.YES.match(limit.getOpenIpWhiteList())) {
      if (!authIpService.isPermitted(requestIp)) {
        throw new ServiceException("当前IP不允许登录：" + requestIp);
      }
    }

    // 是否开启验证码
    if (SystemCodeType.YES.match(limit.getVCode())) {
      captchaProducer.validate(dto.getDeviceId(), request);
    }

    // 判断密码错误次数
    int errorPasswordCount = adminCache.getErrorPasswordCount(dto.getAccount());
    this.checkPasswordErrorCount(limit, errorPasswordCount);

    // 校验安全码
    SysUser user = userService.getByUsername(dto.getAccount());
    if (null == user) {
      throw new UsernameNotFoundException("用户不存在或密码不正确！");
    }

    if (SystemCodeType.YES.match(limit.getOpenGoogleAuth())) {
      this.checkGoogleAuthCode(user.getSafeCode(), dto.getGoogleCode());
    }

    // 解密密码并登陆
    String password = RSAUtils.decrypt(dto.getPassword(), RsaConstant.PRIVATE_KEY);
    UserCredential credential =
        jwtTokenService.signIn(authenticationManager, user.getUserName(), password);

    // 删除密码错误次数记录
    adminCache.cleanErrorPasswordCount(user.getUserName());

    // 更新用户登录信息
    user.setLoginIp(requestIp);
    user.setLoginDate(new Date());
    userService.updateById(user);

    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
    OperatingSystem operatingSystem = userAgent.getOperatingSystem();

    return UserToken.builder()
        .nickName(user.getNickName())
        .accessToken(credential.getAccessToken())
        .refreshToken(credential.getRefreshToken())
        .tokenExpireIn(credential.getTokenExpireIn())
        .loginIp(requestIp)
        .loginDate(new Date())
        .clientType(operatingSystem.getDeviceType().name())
        .deviceType(operatingSystem.name())
        .account(user.getUserName())
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

  private void checkPasswordErrorCount(AdminLoginLimit limit, int errorPasswordCount) {
    boolean checkErrorCount =
        ErrorPasswordLimit.ADMIN_RELIEVE.match(limit.getErrorPwdLimit())
            || (ErrorPasswordLimit.DEFAULT.getKey().equals(limit.getErrorPwdLimit())
                && errorPasswordCount >= limit.getErrorPwdLimit());
    if (checkErrorCount) {
      throw new ServiceException("密码错误次数超限!");
    }
  }

  /**
   * 校验谷歌验证码是否正确
   *
   * @param safeCode String
   * @param googleCode String
   */
  private void checkGoogleAuthCode(String safeCode, String googleCode) {
    if (StringUtils.isBlank(safeCode)) {
      throw new ServiceException("安全码未设置");
    }

    if (StringUtils.isBlank(googleCode)) {
      throw new ServiceException("安全码不能为空!");
    }

    if (!GoogleAuthenticator.authcode(googleCode, safeCode)) {
      throw new ServiceException("安全码不正确!");
    }
  }
}
