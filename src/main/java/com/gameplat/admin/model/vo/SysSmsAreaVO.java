package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("短信区号设置")
@EqualsAndHashCode(callSuper = false)
public class SysSmsAreaVO implements Serializable {

  @ApiModelProperty("主键ID")
  private Long id;
  /** 编码 */
  @ApiModelProperty("编码")
  private String code;
  /** 国家/地区 */
  @ApiModelProperty("国家/地区")
  private String name;

  /** 状态 0 禁用 1 启用 */
  @ApiModelProperty("状态 0 禁用 1 启用")
  private String status;

  @ApiModelProperty("创建人")
  private String createBy;
  /** 创建时间 */
  @ApiModelProperty("创建时间")
  private Date createTime;
  /** 更新人 */
  @ApiModelProperty("更新人")
  private String updateBy;
  /** 更新时间 */
  @ApiModelProperty("更新时间")
  private Date updateTime;

  /** 是否是默认 */
  private Integer isDefault;
}
