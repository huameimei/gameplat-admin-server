package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author kb @Date 2022/3/12 21:17 @Version 1.0
 */
@Data
public class MemberDeviceQueryDTO implements Serializable {

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "设备唯一标识")
  private String deviceClientId;

  @Schema(description = "是否查询设备号详情")
  @NotNull(message = "是否查询设备号详情标识不能为空")
  private Integer isDetail;
}
