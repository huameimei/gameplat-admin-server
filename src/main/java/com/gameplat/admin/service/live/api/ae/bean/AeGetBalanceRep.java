package com.gameplat.admin.service.live.api.ae.bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeGetBalanceRep {

  private String status;

  private String count;

  private String querytime;

  private List<Result> results;

  @Data
  @SuperBuilder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Result {

    private String userId;

    private String balance;

    private String lastModified;
  }
}
