package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description : 每个一级游戏编码对应的分红对象
 * @Author : cc
 * @Date : 2021/12/23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDivideVo {
    @ApiModelProperty(value = "游戏大类")
    private String liveGameName;
    @ApiModelProperty(value = "游戏大类编码")
    private String liveGameCode;
    @ApiModelProperty(value = "一级游戏名称")
    private String name;
    @ApiModelProperty(value = "一级游戏编码")
    private String code;
    @ApiModelProperty(value = "金额比例")
    private BigDecimal amountRatio;
    @ApiModelProperty(value = "上级分配给用户的分红比例")
    private BigDecimal divideRatio;
    @ApiModelProperty(value = "计算分红金额时直接上级的分红计算比例")
    private BigDecimal parentDivideRatio;
    @ApiModelProperty(value = "结算方式1-输赢 2-投注额")
    private Integer settleType;
    @ApiModelProperty(value = "上级分配的分红比例可调整的最大值")
    private BigDecimal maxRatio;
    @ApiModelProperty(value = "上级分配的分红比例可调整的最小值")
    private BigDecimal minRatio;
}
