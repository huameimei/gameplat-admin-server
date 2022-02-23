package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description : 每个一级游戏编码对应的分红对象
 * @Author : cc
 * @Date : 2021/12/23
 */
@Data
public class FissionDivideConfigVo {
    @ApiModelProperty(value = "游戏配置名称")
    private String liveGameName;
    @ApiModelProperty(value = "游戏配置code")
    private String liveGameCode;
    @ApiModelProperty(value = "游戏名称")
    private String name;
    @ApiModelProperty(value = "游戏code")
    private String code;
    @ApiModelProperty(value = "金额比例")
    private BigDecimal amountRatio;
    @ApiModelProperty(value = "结算方式1-输赢 2-投注额")
    private Integer settleType;
}
