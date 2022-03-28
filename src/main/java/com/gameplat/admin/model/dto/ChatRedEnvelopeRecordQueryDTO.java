package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lily
 * @description 红包记录
 * @date 2022/2/15
 */
@Data
public class ChatRedEnvelopeRecordQueryDTO implements Serializable {

  @ApiModelProperty(value = "创建时间")
  private String createTime;

  @NotNull(message = "红包配置id不能为空")
  @ApiModelProperty(value = "红包配置id")
  private Integer redConfigId;
}
