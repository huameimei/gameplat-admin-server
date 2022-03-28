package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatRedEnvelopeDrawVO implements Serializable {

  @ApiModelProperty(value = "主键id")
  private Integer id;

  @ApiModelProperty(value = "会员账号")
  private String nickname;

  @ApiModelProperty(value = "领取时间")
  private Long drawTime;

  @ApiModelProperty(value = "领取金额")
  private Double drawMoney;
}
