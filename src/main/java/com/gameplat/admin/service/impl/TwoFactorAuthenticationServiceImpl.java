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
      Assert.isTrue(StringUtils.isNotEmpty(secret), 10002, "未设置两步验证");
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
    Assert.isTrue(StringUtils.isEmpty(secret), "您已经启用了两步验证，如需更换安全码，请联系客服!");

    // 检查安全码是否被其他用户绑定
    Assert.isFalse(userService.isSecretExist(dto.getSecret()), "安全密钥已被其他用户使用，请刷新页面后重新绑定!");

    // 验证
    Assert.isTrue(codeVerifier.isValidCode(dto.getSecret(), dto.getSafeCode()), "安全码错误，请重新输入!");

    // 绑定
    userService.bindSecret(userId, dto.getSecret());
  }

  @Override
  public void verify2Fa(Long userId, String code) {
    Assert.isTrue(StringUtils.isNotEmpty(code), 10001, "安全码未填写!");
    String retryKey = String.format("%s_%s", KEY_2FA_RETRY_COUNT, userId.toString());
    int retryCount = Optional.ofNullable(redisTemplate.opsForValue().get(retryKey)).orElse(0);
    Assert.isTrue(retryCount < MAX_2FA_RETRY_COUNT, "安全码错误次数过多，请一小时后再试或联系客服！");

    String secret = Assert.notEmpty(userService.getSecret(userId), "认证失败，您还未绑定安全码！");
    if (!codeVerifier.isValidCode(secret, code)) {
      redisTemplate.opsForValue().set(retryKey, ++retryCount, 1, TimeUnit.HOURS);
      throw new ServiceException("安全码错误，请重新输入!");
    }

    // 删除验证码错误次数
    redisTemplate.delete(retryKey);
  }
}
