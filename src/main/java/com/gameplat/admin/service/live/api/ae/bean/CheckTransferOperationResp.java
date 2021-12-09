package com.gameplat.admin.service.live.api.ae.bean;

import lombok.Data;

/** 检查交易状态响应 */
@Data
public class CheckTransferOperationResp extends AeResponse<CheckTransferOperationResp> {

  private String txCode;

  private String txStatus;

  private String transferAmount;

  private String transferType;

  private String balance;
}
