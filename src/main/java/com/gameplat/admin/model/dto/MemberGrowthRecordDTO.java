package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 查询vip成长奖励入参
 * @date 2021/11/23
 */
@Data
public class MemberGrowthRecordDTO implements Serializable {

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
  private Integer type;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;

  private String language;

  private Long memberId;

  private String calcTime;

  private String kindCode;
}
