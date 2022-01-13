package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("区号设置查询DTO")
public class SmsAreaQueryDTO implements Serializable {

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;
    /**
     * 国家/地区
     */
    @ApiModelProperty("国家/地区")
    private String name;

    /**
     * 状态 0 禁用 1 启用
     */
    @ApiModelProperty("状态 0 禁用 1 启用")
    private String status;
}
