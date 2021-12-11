package com.gameplat.admin.service.live.api.ae.bean;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeWithdrawResp implements Serializable {

  private String method;
  private String status;
  private Long databaseId;
  private String currentBalance;
  private String lastModified;
  private String txCode;
  private String amount;

}
