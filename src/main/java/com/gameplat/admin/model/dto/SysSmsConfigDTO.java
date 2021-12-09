package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

// 短信配置DTO
@Data
public class SysSmsConfigDTO implements Serializable {

  @ApiModelProperty(value = "运营商(1表示阿里云)")
  private Integer operator;

  @ApiModelProperty(value = "运营商名称")
  private String operatorName;

  @ApiModelProperty(value = "帐号accessKey")
  private String account;

  @ApiModelProperty(value = "密码secretKey")
  private String password;

  @ApiModelProperty(value = "短信签名")
  private String sign;

  @ApiModelProperty(value = "0禁用 1启用")
  private Integer enable;

  @ApiModelProperty(value = "公共参数")
  private String strOne;

  @ApiModelProperty(value = "公共参数（预留）")
  private String strTwo;

  @ApiModelProperty(value = "最后一次操作（1表示最后一次操作）")
  private Integer lastTime;

  @ApiModelProperty(value = "短信标签")
  private String smsLabel;

  @ApiModelProperty(value = "appId")
  private String appId;

  @ApiModelProperty(value = "短信模板")
  private String tid;

  @ApiModelProperty(value = "国际短息appid")
  private String areaAppId;

  @ApiModelProperty(value = "请求地址")
  private String url;

  @ApiModelProperty(value = "端口")
  private String port;

  @ApiModelProperty(value = "是否开启代理")
  private Boolean isEnableAgency;

  @ApiModelProperty(value = "代理地址")
  private String agencyUrl;

  @ApiModelProperty(value = "代理端口")
  private String agencyPort;
}
