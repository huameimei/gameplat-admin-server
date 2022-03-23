package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** @Author aguai @Description 一级代理 @Date 2022-01-26 */
@Data
public class AgentPlanVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "代理ID")
  private Long agentId;

  @ApiModelProperty(value = "代理账号")
  private String agentName;

  @ApiModelProperty(value = "上级代理ID")
  private Long parentId;

  @ApiModelProperty(value = "上级代理账号")
  private String parentName;

  @ApiModelProperty(value = "代理关系")
  private String agentPath;

  @ApiModelProperty(value = "状态（0禁用 1启用）")
  private Integer status;

  @ApiModelProperty(value = "代理层级")
  private Integer levelNum;

  @ApiModelProperty(value = "下级会员数")
  private Integer subMember;

  @ApiModelProperty(value = "下级代理数")
  private Integer subAgent;

  @ApiModelProperty(value = "分红方案")
  private Integer divideType;

  @ApiModelProperty(value = "方案ID")
  private Long planId;

  @ApiModelProperty(value = "方案名称")
  private String planName;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  private String updateTime;

  @ApiModelProperty(value = "备注信息")
  private String remark;
}
