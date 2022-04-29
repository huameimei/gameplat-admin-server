package com.gameplat.admin.model.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PushCPMessageReq {
  private Long userId;
  private String account;
  private String gameId;
  private String gameName;
  private Long roomId;
  private double winMoney; // 中奖金额
}
