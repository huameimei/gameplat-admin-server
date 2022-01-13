package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 活动查询类
 *
 * @author kenvin
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityInfoQueryDTO implements Serializable {

  @ApiModelProperty(value = "活动类型")
  private Long type;

  @ApiModelProperty(value = "申请方式 （1自动 2主动 3在线客服）")
  private Integer applyType;

  @ApiModelProperty(value = "活动有效状态（1永久有效 2有时限）")
  private Integer validStatus;

  @ApiModelProperty(value = "活动状态,1 有效,0 无效")
  private Integer status;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "关联活动大厅ID")
  private Long activityLobbyId;

  @ApiModelProperty(value = "创建人")
  private String createBy;
}
