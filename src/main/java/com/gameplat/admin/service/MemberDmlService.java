package com.gameplat.admin.service;

import com.gameplat.common.enums.GameCodeEnum;

public interface MemberDmlService {

  /**
   * 计算游戏打码量
   *
   * @param gameCode GameCode
   */
  void calcGameDml(GameCodeEnum gameCode);
}
