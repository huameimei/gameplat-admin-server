package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/** 支付排行榜数据结果集 @Author zak @Date 2022/01/18 21:17:31 */
@Data
public class PayLeaderboardResult {

  @Schema(description = "使用平台")
  private List<PayLeaderboard> usePlatform;

  @Schema(description = "成功支付自动入款率")
  private List<PayLeaderboard> successPayRate;

  @Schema(description = "成功笔数最多")
  private List<PayLeaderboard> successPayNum;

  @Schema(description = "成功金额最多")
  private List<PayLeaderboard> successPayAmount;
}
