package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员活跃度
 *
 * @author 沙漠
 */
@Data
public class MemberActivationDTO {

  @ApiModelProperty(value = "用户名")
  private String username;

  @ApiModelProperty(value = "开始时间")
  private String beginTime;

  @ApiModelProperty(value = "结束时间")
  private String endTime;
}
