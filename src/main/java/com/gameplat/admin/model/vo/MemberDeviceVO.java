package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kb @Date 2022/3/12 21:17 @Version 1.0
 */
@Data
public class MemberDeviceVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "会员账号")
  private String username;

  @Schema(description = "设备名称")
  private String deviceName;

  @Schema(description = "设备唯一标识")
  private String deviceClientId;

  @Schema(description = "用户代理")
  private String deviceUserAgent;

  @Schema(description = "操作系统")
  private String deviceOsType;

  @Schema(description = "IP地址")
  private String ip;

  @Schema(description = "上次登录时间")
  private Date lastLoginTime;
}
