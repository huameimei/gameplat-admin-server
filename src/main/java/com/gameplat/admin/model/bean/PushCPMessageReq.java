package com.gameplat.admin.model.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PushCPMessageReq {
  protected Long userId;
  protected String account;
  protected String gameId;
  protected String gameName;
  protected Long roomId;
  private double winMoney; // 中奖金额
}
