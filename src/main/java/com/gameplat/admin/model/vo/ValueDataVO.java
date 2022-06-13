package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 值类型VO
 *
 * @author admin
 */
@Data
public class ValueDataVO {

  @Schema(description = "数值")
  private String value;

  @Schema(description = "名称")
  private String name;
}
