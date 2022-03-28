package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpreadUnionPackageDTO implements Serializable {

  @ApiModelProperty(value = "联运名称")
  private String unionName;

  @ApiModelProperty(value = "渠道类型")
  private String channel;

  @ApiModelProperty(value = "代理账号")
  private String agentAccount;

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "联盟设置编号")
  private Long unionId;

  @ApiModelProperty(value = "联盟包id")
  private Integer unionPackageId;

  @ApiModelProperty(value = "联盟包名称")
  private String unionPackageName;

  @ApiModelProperty(value = "联运平台")
  private String unionPlatform;

  @ApiModelProperty(value = "推广域名")
  private String promotionDomain;

  @ApiModelProperty(value = "联运专用IOS包下载地址")
  private String iosDownloadUrl;

  @ApiModelProperty(value = "联运专用安卓包下载地址")
  private String appDownloadUrl;

  @ApiModelProperty(value = "联运状态")
  private Integer unionStatus;
}
