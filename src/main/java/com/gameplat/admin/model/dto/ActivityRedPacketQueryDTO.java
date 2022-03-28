package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 红包雨
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityRedPacket查询对象", description = "红包雨")
public class ActivityRedPacketQueryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "状态 0下线 1上线")
  private Integer status;

  @ApiModelProperty(value = "红包雨标题")
  private String realTitle;

  @ApiModelProperty(value = "红包类型（1红包雨，2运营红包）")
  private Integer packetType;
}
