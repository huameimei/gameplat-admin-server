package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatRedEnvelopeDrawVO implements Serializable {

  @Schema(description = "主键id")
  private Integer id;

  @Schema(description = "会员账号")
  private String nickname;

  @Schema(description = "领取时间")
  private Long drawTime;

  @Schema(description = "领取金额")
  private Double drawMoney;
}
