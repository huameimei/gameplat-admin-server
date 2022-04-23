package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kb @Date 2022/3/12 21:17 @Version 1.0
 */
@Data
public class MemberDeviceVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "会员账号")
  private String username;

  @ApiModelProperty(value = "设备名称")
  private String deviceName;

  @ApiModelProperty(value = "设备唯一标识")
  private String deviceClientId;

  @ApiModelProperty(value = "用户代理")
  private String deviceUserAgent;

  @ApiModelProperty(value = "操作系统")
  private String deviceOsType;

  @ApiModelProperty(value = "IP地址")
  private String ip;

  @ApiModelProperty(value = "上次登录时间")
  private Date lastLoginTime;
}
