package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

/**
 * @author lily
 * @description 金币
 * @date 2022/3/1
 */
@Data
@AssertTrue
public class MemberGoldCoinRecordQueryDTO implements Serializable {

  @Schema(description = "订单号")
  private Long memberId;

  @Schema(description = "玩家名字")
  private String account;

  @Schema(description = "来源类型（待定）1 获取成长值、2 爆料扣款")
  private Integer sourceType;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;
}
