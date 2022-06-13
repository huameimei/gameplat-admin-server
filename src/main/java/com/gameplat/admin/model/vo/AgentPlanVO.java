package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/** @Author aguai @Description 一级代理 @Date 2022-01-26 */
@Data
public class AgentPlanVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "代理ID")
  private Long agentId;

  @Schema(description = "代理账号")
  private String agentName;

  @Schema(description = "上级代理ID")
  private Long parentId;

  @Schema(description = "上级代理账号")
  private String parentName;

  @Schema(description = "代理关系")
  private String agentPath;

  @Schema(description = "状态（0禁用 1启用）")
  private Integer status;

  @Schema(description = "代理层级")
  private Integer levelNum;

  @Schema(description = "下级会员数")
  private Integer subMember;

  @Schema(description = "下级代理数")
  private Integer subAgent;

  @Schema(description = "分红方案")
  private Integer divideType;

  @Schema(description = "方案ID")
  private Long planId;

  @Schema(description = "方案名称")
  private String planName;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  private String updateTime;

  @Schema(description = "备注信息")
  private String remark;
}
