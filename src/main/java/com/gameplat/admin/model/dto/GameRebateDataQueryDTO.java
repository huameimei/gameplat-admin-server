package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GameRebateDataQueryDTO implements Serializable {

  /** 游戏平台 */
  private List<String> liveCodeList;

  /** 游戏名称 */
  private List<String> gameKindList;

  /** 会员账号 */
  private String account;

  /** 代理账号 */
  private String superAccount;
  /** 下注开始时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String betStartDate;
  /** 下注结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String betEndDate;

  /***参数****/
  private String userPaths;
}
