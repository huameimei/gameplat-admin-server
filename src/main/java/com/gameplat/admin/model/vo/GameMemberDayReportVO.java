package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class GameMemberDayReportVO implements Serializable {

  /**
   * 主键
   */
  private Long id;

  /**
   * 账号编号
   */
  private Long memberId;

  /**
   * 账号
   */
  private String account;

  /**
   * 用户真实姓名
   */
  private String realName;

  /**
   * 代理路径
   */
  private String userPaths;

  /**
   * 游戏平台
   */
  private String platformCode;

  /**
   * 游戏名称
   */
  private String name;

  /**
   * 游戏子类型
   */
  private String gameKind;

  private String gameType;

  /**
   * 年月日
   */
  private Date statTime;

   /**
   * 添加时间
   */
  private Date createTime;

  /**
   * 投注金额
   */
  private BigDecimal betAmount;

  /**
   * 有效投注额
   */
  private BigDecimal validAmount;


  /**
   * 中奖金额
   */
  private Double winAmount;

  /**
   * 下注笔数
   */
  private Long betCount;
  /**
   * 中奖数
   */
  private Long winCount;

  private String userLevel;

}
