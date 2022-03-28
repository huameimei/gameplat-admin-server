package com.gameplat.admin.model.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实时支付排行榜VO
 *
 * @Author zak
 * @Date 2022/01/18 19:41:05
 */
@Data
public class PayLeaderboardParam {
    @ApiModelProperty("三方支付接口名称")
    private String interfaceName;

    @ApiModelProperty("三方支付接口code")
    private String interfaceCode;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("排行榜统计数量")
    private Integer rankCount;

    @ApiModelProperty("支付类型")
    private String payTypeCode;
}
