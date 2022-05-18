package com.gameplat.admin.model.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实时支付排行榜VO
 *
 * @Author zak
 * @Date 2022/01/18 19:41:05
 */
@Data
public class PayLeaderboardParam {
  @Schema(description = "三方支付接口名称")
  private String interfaceName;

  @Schema(description = "三方支付接口code")
  private String interfaceCode;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;

  @Schema(description = "排行榜统计数量")
  private Integer rankCount;

  @Schema(description = "支付类型")
  private String payTypeCode;
}
