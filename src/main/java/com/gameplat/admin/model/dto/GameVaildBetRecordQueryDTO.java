package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GameVaildBetRecordQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 唯一编码 */
  private String billNo;

  /** 用户名 */
  private String account;

  /** 平台编码 */
  private String platformCode;

  /** 游戏分类 */
  private String gameKind;

  /** 开始时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private String beginTime;

  /** 结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private String endTime;

  /** 结束时间 */
  private String state;

  /** 1 -- 投注时间, 2 -- 三方时间, 3 -- 结算时间, 4 -- 报表时间 */
  private Integer timeType;

  private String userPaths;

  private String liveGameKind;

  private String superAccount;

  private String liveGameKindList;

  private String liveGameSuperType;

  private String liveCode;

  /** 开始时间 */
  private Date createTime;

  /** 结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private Date overTime;
}
