package com.gameplat.admin.model.vo;

import lombok.Data;

@Data
public class GameListOneVO {

  /** */
  private String liveCode;
  /** */
  private String walletCode;

  /** 一级游戏编码 */
  private String firstCode;

  /** 游戏code */
  private String gameCode;

  /** 是否支持试玩 */
  private String trial;
}
