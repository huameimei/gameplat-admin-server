package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description : VIP会员签到历史记录入参 @Author : lily @Date : 2021/12/07
 */
@Data
public class MemberVipSignHistoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "会员ID")
  private Long userId;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "签到时间-开始")
  private String signBeginTime;

  @Schema(description = "签到时间-结束")
  private String signEndTime;
}
