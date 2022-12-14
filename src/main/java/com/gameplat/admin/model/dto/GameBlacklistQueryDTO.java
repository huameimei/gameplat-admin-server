package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameBlacklistQueryDTO implements Serializable {

  /** 真人游戏 */
  private String liveCategory;
  /** 真人黑名单类型 */
  private String blackType;
  /** 会员账号 会员层级 */
  private String userAccount;

  /** 会员层级 */
  private String userLevel;
}
