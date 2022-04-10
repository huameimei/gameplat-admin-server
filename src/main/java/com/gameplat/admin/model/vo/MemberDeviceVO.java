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

  @ApiModelProperty(value = "会员账号")
  private String username;

  @ApiModelProperty(value = "设备唯一标识")
  private String deviceClientId;

  @ApiModelProperty(value = "上次登录时间")
  private Date lastLoginTime;

  @ApiModelProperty(value = "是否重复 0未重复 1重复")
  private Integer isRepetition;

  @ApiModelProperty(value = "同一个设备号的会员数量")
  private Integer deviceNum;

}
