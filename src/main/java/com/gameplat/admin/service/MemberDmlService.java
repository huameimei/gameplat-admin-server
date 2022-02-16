package com.gameplat.admin.service;

import com.gameplat.common.enums.GameKindEnum;

public interface MemberDmlService {

  /**
   * 计算游戏打码量
   *
   * @param gameCode GameCode
   */
  void calcGameDml(GameKindEnum gameCode);
}
