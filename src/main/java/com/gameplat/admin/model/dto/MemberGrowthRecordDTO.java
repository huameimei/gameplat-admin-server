package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 查询vip成长奖励入参
 * @date 2021/11/23
 */
@Data
public class MemberGrowthRecordDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String userName;

  @ApiModelProperty(value = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
  private Integer type;

  @ApiModelProperty(value = "开始时间")
  private String startTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;

  private String language;
}
