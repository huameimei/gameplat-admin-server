package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/3/12 17:14 @Version 1.0
 */
@Data
public class SysUserAuthVo implements Serializable {

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
}
