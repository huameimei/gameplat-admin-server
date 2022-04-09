package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author kb @Date 2022/3/12 21:17 @Version 1.0
 */
@Data
public class MemberDeviceQueryDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String username;

  @ApiModelProperty(value = "设备唯一标识")
  private String deviceClientId;

  @ApiModelProperty(value = "是否查询设备号详情")
  @NotNull(message = "是否查询设备号详情标识不能为空")
  private Integer isDetail;

}
