package com.gameplat.admin.model.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GameBetRecordQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 唯一编码 */
  private String billNo;

  /** 用户名 */
  private String account;

  /** 游戏账号 */
  private String gameAccount;

  /** 平台编码 */
  private String platformCode;

  /** 游戏分类 */
  private String gameKind;

  /** 游戏编码 */
  private String playCode;

  /** 结算状态 */
  private String settle;

  /** 开始时间 */
  private String beginTime;

  /** 结束时间 */
  private String endTime;

  /** 时间 */
  private String time;

  /** 1 -- 投注时间, 2 -- 三方时间, 3 -- 结算时间, 4 -- 报表时间 */
  private Integer timeType;

  private String userPaths;

  private String proxyAccount;

  /** 是否只查询直属下级 */
  private Integer isDirectly;

  private String gameKindList;

  private String gameType;

  private String gameCode;
}
