package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameRecycleVO implements Serializable {
  private String platfromCode;

  /** 0 : 成功 1 : 失败 */
  private int status;

  private String platformName;

  private String errorMsg;
}
