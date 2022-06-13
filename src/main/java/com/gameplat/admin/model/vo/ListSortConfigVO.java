package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListSortConfigVO {

  @Schema(description = "序号, 排序值")
  private Integer sort;

  @Schema(description = "名称")
  private String name;

  @Schema(description = "开关状态")
  private Boolean onOff;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "类型")
  private String type;
}
