package com.gameplat.admin.model.domain;


import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("game_config")
@Builder
@AllArgsConstructor
public class GameConfig implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  @ApiModelProperty("游戏平台编码")
  private String platformCode;

  @ApiModelProperty("游戏配置 json")
  private String config;

  @ApiModelProperty("租户标识")
  private String tenantCode;

  @ApiModelProperty("三方代理编号  游戏平台_三方代理号")
  private String agentCode;

  @ApiModelProperty("币种")
  private String currency;

  @ApiModelProperty("开关 0关 1开")
  private Integer IsOpen;

  @ApiModelProperty("备注")
  private String remark;

  @ApiModelProperty("创建时间")
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @ApiModelProperty("创建者")
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @ApiModelProperty("更新者")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  @ApiModelProperty("更新时间")
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;
}
