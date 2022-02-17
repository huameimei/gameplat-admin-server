package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/14
 */
@Data
public class ChatRedEnvelopeQueryDTO implements Serializable {

    @ApiModelProperty(value = "红包名称")
    private String name;

    @ApiModelProperty(value = "开始发送红包时间(时间戳)")
    private Long startTime;

    @ApiModelProperty(value = "禁止_启用1:启用，0：禁用")
    private Integer open;
}
