package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameBetRecordQueryDTO implements Serializable {

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
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String beginTime;

  /** 结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private String endTime;

  /** 1 -- 投注时间, 2 -- 三方时间, 3 -- 结算时间, 4 -- 报表时间 */
  private Integer timeType;

  private String userPaths;

  private String superAccount;

    private String gameKindList;

  private String liveGameSuperType;

}
