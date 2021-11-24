package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description vip等级汇总查询入参
 * @date 2021/11/24
 */
@Data
public class MemberGrowthStatisDTO implements Serializable {

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "创建开始时间")
    private String startTime;

    @ApiModelProperty(value = "创建结束时间")
    private String endTime;
}
