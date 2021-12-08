package com.gameplat.admin.service.live.api.ae.bean;

import java.util.List;
import lombok.Data;

@Data
public class GetBalanceRep {

  private String status;

  private String count;

  private String querytime;

  private List<Result> results;

  @Data
  public static class Result {

    private String userId;

    private String balance;

    private String lastModified;
  }
}
