package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import com.gameplat.admin.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public String encrypt(String password) {
    return passwordEncoder.encode(password);
  }

  @Value("${security.rsa.privateKey}")
  private String privateKey;

  @Override
  public String encrypt(String password, String salt) {
    Assert.notEmpty(salt, "密码盐不能为空");
    String cipherText = MD5.create().digestHex(salt.concat("@").concat(password));
    return passwordEncoder.encode(cipherText);
  }

  @Override
  public String decode(String password) {
    return new RSA(privateKey, null).decryptStr(password, KeyType.PrivateKey);
  }
}
