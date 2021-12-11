package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 检查交易状态响应 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeCheckTransferOperationResp extends AeResponse<AeCheckTransferOperationResp> {

  private String txCode;

  private String txStatus;

  private String transferAmount;

  private String transferType;

  private String balance;
}
