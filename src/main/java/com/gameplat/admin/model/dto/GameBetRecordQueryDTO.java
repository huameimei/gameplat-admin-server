package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import lombok.Data;

@Data
public class GameBetRecordQueryDTO implements Serializable {

  private String account;

  /** 局号 */
  private String gameNo;

  /** 订单号 */
  private String billNo;

  /** 报表统计时间 */
  private String statTime;
  /** 下注开始时间*/
  @JsonFormat(locale ="zh", timezone ="GMT+8", pattern = "yyyy-MM-dd")
  private String betStartDate;
  /** 下注结束时间*/
  @JsonFormat(locale ="zh", timezone ="GMT+8", pattern = "yyyy-MM-dd")
  private String betEndDate;

  /**
   *  0 -- 下注时间,
   *  1 -- 打码量统计时间
   *  2 -- 返水统计时间,
   *  3 -- 北京时间,
   *  4 -- 报表统计时间
   */
  private Integer timeType = 0;

  private String gameKind;

  private String userPaths;

  private String liveGameKind;

  private String superAccount;

  private String liveGameKindList;

  private String liveGameSuperType;

  /**
   *  游戏平台
   */
  private String liveCode;
}
