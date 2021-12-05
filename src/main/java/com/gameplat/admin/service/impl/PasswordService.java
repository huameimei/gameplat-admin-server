package com.gameplat.admin.service.impl;

import java.security.PrivateKey;

public interface PasswordService {

  String encode(String password, String salt);

  String encode(String password, PrivateKey privateKey);
}
