package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class GameRebateDataVO implements Serializable {

  private Long id;

  /** 账号编号 */
  private Long memberId;

  /** 账号 */
  private String account;

  /** 用户真实姓名 */
  private String realName;

  /** 代理路径 */
  private String userPaths;

  /** 真人游戏 */
  private String platformCode;

  /** 游戏子类型 */
  private String gameKind;

  private String statTime;

  /** 添加时间 */
  private Date createTime;

  private BigDecimal betAmount;

  private BigDecimal validAmount;

  private BigDecimal winAmount;

  private Integer betCount;

  private Integer winCount;

  private String gameType;

  private String name;
}
