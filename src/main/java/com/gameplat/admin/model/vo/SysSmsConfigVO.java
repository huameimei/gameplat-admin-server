package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
@ApiModel(value = "短信运营商VO", description = "短信运营商VO")
public class SysSmsConfigVO implements Serializable {

  @ApiModelProperty(value = "运营商（1:阿里云 2:聚合）")
  private Integer operator;

  @ApiModelProperty(value = "运营商名称")
  private String operatorName;

  @ApiModelProperty(value = "短信签名")
  private String sign;

  @ApiModelProperty(value = "帐号（聚合）")
  private String account;

  @ApiModelProperty(value = "密码（聚合）")
  private String password;

  @ApiModelProperty(value = "0禁用 1启用")
  private Integer enable;

  @ApiModelProperty(value = "短信标签")
  private String smsLabel;

  @ApiModelProperty(value = "appId")
  private String appId;

  @ApiModelProperty(value = "短信模板")
  private String tid;

  @ApiModelProperty(value = "请求地址")
  private String url;

  @ApiModelProperty(value = "端口")
  private String port;

  @ApiModelProperty(value = "国际短息appid")
  private String areaAppId;

  @ApiModelProperty(value = "是否开启代理")
  private Boolean isEnableAgency;

  @ApiModelProperty(value = "代理地址")
  private String agencyUrl;

  @ApiModelProperty(value = "代理端口")
  private String agencyPort;

  @ApiModelProperty(value = "是否支持国际短信")
  private Boolean isIntl;

  @ApiModelProperty(value = "国际短信appid")
  private String intlAppid;

  @ApiModelProperty(value = "国际短信URL")
  private String intlUrl;

  @ApiModelProperty(value = "国际短信模版")
  private String intlTid;
}
