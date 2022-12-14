package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import com.gameplat.admin.service.PasswordService;
import com.gameplat.security.config.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private SecurityProperties securityProperties;

  @Override
  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public String encode(String password, String salt) {
    Assert.notEmpty(salt, "密码盐不能为空");
    String cipherText = MD5.create().digestHex(salt.concat("@").concat(password));
    return passwordEncoder.encode(cipherText);
  }

  @Override
  public String decrypt(String password) {
    return new RSA(securityProperties.getPassword().getPrivateKey(), null)
        .decryptStr(password, KeyType.PrivateKey);
  }

  @Override
  public String encryptCashPassword(String cashPassword) {
    MD5 md5 = MD5.create();
    return md5.digestHex(md5.digestHex(cashPassword));
  }
}
