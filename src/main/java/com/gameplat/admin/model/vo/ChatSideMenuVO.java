package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSideMenuVO implements Serializable {

  @ApiModelProperty(value = "菜单名称")
  private String name;

  @ApiModelProperty(value = "自定义LOGO")
  private String url;

  @ApiModelProperty(value = "排序")
  private int sort;

  @ApiModelProperty(value = "是否启用")
  private int open;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty(value = "类型")
  private String type;
}
