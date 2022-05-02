package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListSortConfigVO {

  @ApiModelProperty("序号, 排序值")
  private Integer sort;

  @ApiModelProperty("名称")
  private String name;

  @ApiModelProperty("开关状态")
  private Boolean onOff;

  @ApiModelProperty("备注")
  private String remark;

  @ApiModelProperty("类型")
  private String type;
}
