package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 聊天室红包
 *
 * @author lily
 */
@Data
public class ChatRedEnvelopeVO implements Serializable {

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "红包配置ID")
  private Long redConfigId;

  @Schema(description = "红包名称")
  private String name;

  @Schema(description = "所属聊天室")
  private Integer roomId;

  @Schema(description = "单次红包个数")
  private Integer single;

  @Schema(description = "单次红包总额(元）")
  private Double money;

  @Schema(description = "单次红包失效时间(单位:分钟)：")
  private Integer expireDate;

  @Schema(description = "开始发送红包时间")
  private Long startTime;

  @Schema(description = "发送间隔(单位:分钟)")
  private Integer sendInterval;

  @Schema(description = "发送总次数")
  private Integer totalSent;

  @Schema(description = "当前已发次数")
  private int currentCount;

  @Schema(description = "禁止_启用1:启用，0：禁用")
  private Integer open;
}
