package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.GoogleAuthCodeVO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.service.TwoFactorAuthenticationService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.security.SecurityUserHolder;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwoFactorAuthenticationServiceImpl implements TwoFactorAuthenticationService {

  private static final String KEY_2FA_RETRY_COUNT = "KEY_2FA_RETRY_COUNT";

  private static final int MAX_2FA_RETRY_COUNT = 3;

  @Autowired private SysUserService userService;

  @Autowired private RedisTemplate<String, Integer> redisTemplate;

  @Autowired private CodeVerifier codeVerifier;

  @Autowired private SecretGenerator secretGenerator;

  @Autowired private QrDataFactory qrDataFactory;

  @Autowired private QrGenerator qrGenerator;

  @Autowired private LimitInfoService limitInfoService;

  @Override
  public boolean isEnabled() {
    return Optional.ofNullable(limitInfoService.getAdminLimit())
        .map(AdminLoginLimit::getGoogleAuthSwitch)
        .map(EnableEnum::isEnabled)
        .orElse(false);
  }

  @Override
  public void isEnabled2Fa() {
    if (this.isEnabled()) {
      String secret = userService.getSecret(SecurityUserHolder.getUserId());
      Assert.isTrue(StringUtils.isNotEmpty(secret), 10002, "?????????????????????");
    }
  }

  @Override
  @SneakyThrows
  public GoogleAuthCodeVO create2Fa(String username) {
    String secret = secretGenerator.generate();
    QrData data =
        qrDataFactory.newBuilder().label(username).secret(secret).issuer("AppName").build();

    String image =
        Utils.getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());

    return GoogleAuthCodeVO.builder().img(image).secret(secret).build();
  }

  @Override
  @SentinelResource(value = "bindSecret")
  public void bindSecret(GoogleAuthDTO dto) {
    Long userId = SecurityUserHolder.getUserId();
    String secret = userService.getSecret(userId);
    Assert.isTrue(StringUtils.isEmpty(secret), "????????????????????????????????????????????????????????????????????????!");

    // ??????????????????????????????????????????
    Assert.isFalse(userService.isSecretExist(dto.getSecret()), "?????????????????????????????????????????????????????????????????????!");

    // ??????
    Assert.isTrue(codeVerifier.isValidCode(dto.getSecret(), dto.getSafeCode()), "?????????????????????????????????!");

    // ??????
    userService.bindSecret(userId, dto.getSecret());
  }

  @Override
  public void verify2Fa(Long userId, String code) {
    Assert.isTrue(StringUtils.isNotEmpty(code), 10001, "??????????????????!");
    String retryKey = String.format("%s_%s", KEY_2FA_RETRY_COUNT, userId.toString());
    int retryCount = Optional.ofNullable(redisTemplate.opsForValue().get(retryKey)).orElse(0);
    Assert.isTrue(retryCount < MAX_2FA_RETRY_COUNT, "?????????????????????????????????????????????????????????????????????");

    String secret = Assert.notEmpty(userService.getSecret(userId), "??????????????????????????????????????????");
    if (!codeVerifier.isValidCode(secret, code)) {
      redisTemplate.opsForValue().set(retryKey, ++retryCount, 1, TimeUnit.HOURS);
      throw new ServiceException("?????????????????????????????????!");
    }

    // ???????????????????????????
    redisTemplate.delete(retryKey);
  }
}
