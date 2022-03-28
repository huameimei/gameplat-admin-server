package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "订单号")
  private String orderNo;

  @ApiModelProperty(value = "订单号")
  private Long memberId;

  @ApiModelProperty(value = "玩家名字")
  private String account;

  @ApiModelProperty(value = "来源类型（待定）1 获取成长值、2 爆料扣款")
  private Integer sourceType;

  @ApiModelProperty(value = "变动前金币数")
  private Integer beforeBalance;

  @ApiModelProperty(value = "变动金币数")
  private Integer amount;

  @ApiModelProperty(value = "变动后金币数")
  private Integer afterBalance;

  @ApiModelProperty(value = "备注")
  private String remark;
}
