package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgentContactDTO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "状态")
  private String contactType;

  @Schema(description = "Logo")
  private String contactLogo;

  @Schema(description = "联系方式")
  private String contact;

  @Schema(description = "联系详情")
  private String contactDetail;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private String createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private String updateTime;
}
