package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author lily
 * @description 查询福利入参
 * @date 2021/11/22
 */

@Data
public class MemberWealDTO implements Serializable {

    @ApiModelProperty(value = "福利名称")
    private String name;

    @ApiModelProperty(value = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
    private Integer type;

    @ApiModelProperty(value = "状态 0:未结算  1：未派发   2：已派发  3：已回收")
    private Integer status;

    @ApiModelProperty(value = "周期  开始时间")
    private String startDate;

    @ApiModelProperty(value = "周期  结束时间")
    private String endDate;
}
