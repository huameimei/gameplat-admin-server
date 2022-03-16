package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameRecycleVO implements Serializable {
  private String platfromCode;

  /** 0 : 成功 1 : 失败 */
  private int status;

  private String platformName;

  private String errorMsg;
}
