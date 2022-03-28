package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author kb @Date 2022/3/12 17:14 @Version 1.0
 */
@Data
public class SysUserAuthDto implements Serializable {

  @ApiModelProperty(value = "主键")
  private Integer authId;

  @ApiModelProperty(value = "身份认证中心url")
  private String idcardAuthUrl;

  @ApiModelProperty(value = "身份认证中心key")
  private String idcardAuthKey;

  @ApiModelProperty(value = "银行认证中心url")
  private String bankAuthUrl;

  @ApiModelProperty(value = "银行认证中心key")
  private String bankAuthKey;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;
}
