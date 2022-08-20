package com.gameplat.admin.service;

public interface TransferAgentService {
  void transferData(String lockKey, String account, String originSuperPath, String newSuperPath);
}
