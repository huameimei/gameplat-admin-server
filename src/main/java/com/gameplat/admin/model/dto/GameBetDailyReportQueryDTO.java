package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class GameBetDailyReportQueryDTO implements Serializable  {

  private String liveGameSuperType;

  private String platformCode;

  private String liveGameKind;

  private String liveGameKindList;

  /**
   * 会员账号
   */
  private String account;

  /**
   * 代理账号
   */
  private String superAccount;

  /**
   * 开始时间
   */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String beginTime;

  /**
   * 结束时间
   */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String endTime;

  /**
   * 1 - 投注时间(北京时间),
   * 2 - 三方时间
   */
  private Integer timeType;

  private List<String> platformCodeList;

  private List<String> gameKindList;

  /***参数****/
  private String userPaths;
}
