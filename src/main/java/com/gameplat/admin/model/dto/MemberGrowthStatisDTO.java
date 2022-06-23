package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description vip等级汇总查询入参
 * @date 2021/11/24
 */
@Data
public class MemberGrowthStatisDTO implements Serializable {

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "创建开始时间")
  private String startTime;

  @Schema(description = "创建结束时间")
  private String endTime;
}
