package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description 代理联系方式
 * @date 2022/1/3
 */
@Data
@ApiModel(value = "代理联系方式VO", description = "代理联系方式VO")
public class AgentContacaVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "状态")
  private String contactType;

  @ApiModelProperty(value = "Logo")
  private String contactLogo;

  @ApiModelProperty(value = "联系方式")
  private String contact;

  @ApiModelProperty(value = "联系详情")
  private String contactDetail;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  private String createTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  private String updateTime;
}
