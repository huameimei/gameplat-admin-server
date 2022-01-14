package com.gameplat.admin.service;

public interface PasswordService {

  String encrypt(String password, String salt);

  String decode(String password);
}
