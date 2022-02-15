package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 聊天室红包
 * @author lily
 */

@Data
public class ChatRedEnvelopeVO implements Serializable {

  @ApiModelProperty(value = "主键ID")
  private Long id;

  @ApiModelProperty(value = "红包名称")
  private String name;

  @ApiModelProperty(value = "所属聊天室")
  private Integer roomId;

  @ApiModelProperty(value = "单次红包个数")
  private Integer single;

  @ApiModelProperty(value = "单次红包总额(元）")
  private Double money;

  @ApiModelProperty(value = "单次红包失效时间(单位:分钟)：")
  private Integer expireDate;

  @ApiModelProperty(value = "开始发送红包时间")
  private Long startTime;

  @ApiModelProperty(value = "发送间隔(单位:分钟)")
  private Integer sendInterval;

  @ApiModelProperty(value = "发送总次数")
  private Integer totalSent;

  @ApiModelProperty(value = "当前已发次数")
  private int currentCount;

  @ApiModelProperty(value = "禁止_启用1:启用，0：禁用")
  private Integer open;
}
