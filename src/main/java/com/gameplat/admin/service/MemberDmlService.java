package com.gameplat.admin.service;

import com.gameplat.common.enums.GameCode;

public interface MemberDmlService {

  /**
   * 计算游戏打码量
   *
   * @param gameCode GameCode
   */
  void calcGameDml(GameCode gameCode);
}
