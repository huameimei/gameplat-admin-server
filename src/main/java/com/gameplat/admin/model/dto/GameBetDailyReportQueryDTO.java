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
  /** 下注开始时间*/
  @JsonFormat(locale ="zh", timezone ="GMT+8", pattern = "yyyy-MM-dd")
  private String betStartDate;
  /** 下注结束时间*/
  @JsonFormat(locale ="zh", timezone ="GMT+8", pattern = "yyyy-MM-dd")
  private String betEndDate;

  private List<String> platformCodeList;

  private List<String> gameKindList;

  /***参数****/
  private String userPaths;
}
