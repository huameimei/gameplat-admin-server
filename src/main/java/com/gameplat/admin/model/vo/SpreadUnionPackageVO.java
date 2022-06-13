package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpreadUnionPackageVO implements Serializable {

  @Schema(description = "联盟名称")
  private String unionName;

  @Schema(description = "代理账号")
  private String agentAccount;

  @Schema(description = "渠道类型")
  private String channel;

  @Schema(description = "联盟Id")
  private Long unionId;

  @Schema(description = "主键")
  private Long id;

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

  @Schema(description = "注册用户")
  private Integer sumUser;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;
}
