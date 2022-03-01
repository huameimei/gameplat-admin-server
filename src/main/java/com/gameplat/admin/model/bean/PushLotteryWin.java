package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PushLotteryWin implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "不推送会员账号")
  private String blackAccounts;

  @ApiModelProperty(value = "是否开启")
  private Integer isOpen;

  @ApiModelProperty(value = "中奖最高前几名")
  private Integer topNum;

  @ApiModelProperty(value = "最低中奖金额")
  private Double winMoney;

  @ApiModelProperty(value = "房间进入特效")
  private String vipEnterLevels;
}
