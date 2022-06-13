package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class PushLotteryWin implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "不推送会员账号")
  private String blackAccounts;

  @Schema(description = "是否开启")
  private Integer isOpen;

  @Schema(description = "中奖最高前几名")
  private Integer topNum;

  @Schema(description = "最低中奖金额")
  private Double winMoney;

  @Schema(description = "房间进入特效")
  private String vipEnterLevels;
}
