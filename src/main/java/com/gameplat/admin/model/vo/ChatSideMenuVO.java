package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSideMenuVO implements Serializable {

  @Schema(description = "菜单名称")
  private String name;

  @Schema(description = "自定义LOGO")
  private String url;

  @Schema(description = "排序")
  private int sort;

  @Schema(description = "是否启用")
  private int open;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "类型")
  private String type;
}
