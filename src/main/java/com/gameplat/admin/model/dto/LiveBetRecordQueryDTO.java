package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class LiveBetRecordQueryDTO implements Serializable {

  private String account;

  /** 局号 */
  private String gameNo;

  /** 订单号 */
  private String billNo;

  /** 报表统计时间 */
  private String statTime;
  /** 下注开始时间*/
  private Date betStartDate;
  /** 下注结束时间*/
  private Date betEndDate;

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
}
