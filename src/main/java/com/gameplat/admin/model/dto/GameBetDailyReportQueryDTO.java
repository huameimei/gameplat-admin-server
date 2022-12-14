package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GameBetDailyReportQueryDTO implements Serializable {

  private String gameType;

  private String platformCode;

  /** 会员账号 */
  private String account;

  /** 代理账号 */
  private String superAccount;

  /** 开始时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String beginTime;

  /** 结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String endTime;

  /** 1 - 投注时间(北京时间), 2 - 三方时间 */
  private Integer timeType;

  private List<String> platformCodeList;

  private List<String> gameKindList;

  /***参数****/
  private String userPaths;
}
