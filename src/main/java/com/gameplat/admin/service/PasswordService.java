package com.gameplat.admin.service;

import java.security.PrivateKey;

public interface PasswordService {

  String encode(String password, String salt);

  String decode(String password, String privateKey);

  String decode(String password, PrivateKey privateKey);
}
