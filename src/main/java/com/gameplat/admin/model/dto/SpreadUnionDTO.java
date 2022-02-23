package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class SpreadUnionDTO implements Serializable {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "联盟名称")
    private String unionName;

    @ApiModelProperty(value = "代理账号")
    private String agentAccount;

    @ApiModelProperty(value = "渠道类型")
    private String channel;

    /**
     * 日期
     */
    @NotEmpty(message = "开始时间不能为空")
    private String startTime;
    @NotEmpty(message = "结束时间不能为空")
    private String endTime;

}
