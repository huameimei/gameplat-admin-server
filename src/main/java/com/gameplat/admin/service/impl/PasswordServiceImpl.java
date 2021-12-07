package com.gameplat.admin.service.impl;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.digest.MD5;
import com.gameplat.base.common.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
public class PasswordServiceImpl implements PasswordService {

  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public String encode(String password, String salt) {
    String cipherText = MD5.create().digestHex(salt.concat("@").concat(password));
    return passwordEncoder.encode(cipherText);
  }

  @Override
  public String decode(String password, String privateKey) {
    PrivateKey key = KeyUtil.generateRSAPrivateKey(Base64.decode(privateKey));
    return this.decode(password, key);
  }

  @Override
  public String decode(String password, PrivateKey privateKey) {
    return new RSA(privateKey, null).decryptStr(password, KeyType.PrivateKey);
  }
}
