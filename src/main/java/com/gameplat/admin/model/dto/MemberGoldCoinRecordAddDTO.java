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
public class MemberGoldCoinRecordAddDTO implements Serializable {

  @Schema(description = "订单号")
  private String orderNo;

  @Schema(description = "订单号")
  private Long memberId;

  @Schema(description = "玩家名字")
  private String account;

  @Schema(description = "来源类型（待定）1 获取成长值、2 爆料扣款")
  private Integer sourceType;

  @Schema(description = "变动前金币数")
  private Integer beforeBalance;

  @Schema(description = "变动金币数")
  private Integer amount;

  @Schema(description = "变动后金币数")
  private Integer afterBalance;

  @Schema(description = "备注")
  private String remark;
}
