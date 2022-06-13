package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysSmsAreaVO implements Serializable {

  @Schema(description = "主键ID")
  private Long id;
  /** 编码 */
  @Schema(description = "编码")
  private String code;
  /** 国家/地区 */
  @Schema(description = "国家/地区")
  private String name;

  /** 状态 0 禁用 1 启用 */
  @Schema(description = "状态 0 禁用 1 启用")
  private String status;

  @Schema(description = "创建人")
  private String createBy;
  /** 创建时间 */
  @Schema(description = "创建时间")
  private Date createTime;
  /** 更新人 */
  @Schema(description = "更新人")
  private String updateBy;
  /** 更新时间 */
  @Schema(description = "更新时间")
  private Date updateTime;

  /** 是否是默认 */
  private Integer isDefault;
}
