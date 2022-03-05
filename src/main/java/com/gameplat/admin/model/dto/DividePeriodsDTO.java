package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description : 分红期数
 * @Author : cc
 * @Date : 2022/2/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DividePeriodsDTO {
    private Long id;

    @ApiModelProperty(value = "期数结算起始日期")
    private String startDate;

    @ApiModelProperty(value = "期数结算截止日期")
    private String endDate;

    @ApiModelProperty(value = "结算状态 1 未结算  2 已结算")
    private Integer settleStatus;

    @ApiModelProperty(value = "派发状态 1 未派发  2 已派发 3 已回收")
    private Integer grantStatus;

    @ApiModelProperty(value = "结算时业主开启的分红模式 1 固定比例  2 裂变  3 层层代 4 平级")
    private Integer divideType;
}
