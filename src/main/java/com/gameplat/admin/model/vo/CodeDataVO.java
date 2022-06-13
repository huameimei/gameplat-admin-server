package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 类型编码类型的VO */
@Data
public class CodeDataVO {

  @Schema(description = "编码")
  private String code;

  @Schema(description = "名称")
  private String name;
}
