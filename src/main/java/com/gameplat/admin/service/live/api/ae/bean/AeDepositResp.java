package com.gameplat.admin.service.live.api.ae.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeDepositResp extends AeResponse<AeDepositResp> {

    private String amount;

    private String method;

    private String currentBalance;

    private String databaseId;

    private String lastModified;

    private String txCode;

}
