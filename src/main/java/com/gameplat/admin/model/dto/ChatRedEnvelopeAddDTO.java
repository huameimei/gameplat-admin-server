package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/14
 */
@Data
public class ChatRedEnvelopeAddDTO implements Serializable {

  @NotNull(message = "红包名称不能为空！")
  @ApiModelProperty(value = "红包名称")
  private String name;

  @NotNull(message = "所属聊天室不能为空！")
  @ApiModelProperty(value = "所属聊天室")
  private Integer roomId;

  @NotNull(message = "单次红包个数不能为空！")
  @ApiModelProperty(value = "单次红包个数")
  private Integer single;

  @NotNull(message = "单次红包总额不能为空！")
  @ApiModelProperty(value = "单次红包总额(元）")
  private Double money;

  @NotNull(message = "单次红包失效时间不能为空！")
  @ApiModelProperty(value = "单次红包失效时间(单位:分钟)：")
  private Integer expireDate;

  @NotNull(message = "开始发送红包时间不能为空！")
  @ApiModelProperty(value = "开始发送红包时间")
  private Long startTime;

  @NotNull(message = "发送间隔不能为空！")
  @ApiModelProperty(value = "发送间隔(单位:分钟)")
  private Integer sendInterval;

  @NotNull(message = "发送总次数不能为空！")
  @ApiModelProperty(value = "发送总次数")
  private Integer totalSent;
}
