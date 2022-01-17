package com.gameplat.admin.service;

public interface PasswordService {

  String encode(String password);

  String encode(String password, String salt);

  String decrypt(String password);
}
