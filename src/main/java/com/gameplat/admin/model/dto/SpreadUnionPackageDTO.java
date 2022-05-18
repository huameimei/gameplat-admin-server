package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SpreadUnionPackageDTO implements Serializable {

  @Schema(description = "联运名称")
  private String unionName;

  @Schema(description = "渠道类型")
  private String channel;

  @Schema(description = "代理账号")
  private String agentAccount;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "联盟设置编号")
  private Long unionId;

  @Schema(description = "联盟包id")
  private Integer unionPackageId;

  @Schema(description = "联盟包名称")
  private String unionPackageName;

  @Schema(description = "联运平台")
  private String unionPlatform;

  @Schema(description = "推广域名")
  private String promotionDomain;

  @Schema(description = "联运专用IOS包下载地址")
  private String iosDownloadUrl;

  @Schema(description = "联运专用安卓包下载地址")
  private String appDownloadUrl;

  @Schema(description = "联运状态")
  private Integer unionStatus;
}
