package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员活跃度
 *
 * @author 沙漠
 */
@Data
public class MemberActivationDTO {

  @Schema(description = "用户名")
  private String username;

  @Schema(description = "开始时间")
  private String beginTime;

  @Schema(description = "结束时间")
  private String endTime;
}
