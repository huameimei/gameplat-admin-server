package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aBen
 * @date 2022/3/6 17:28
 * @desc 游戏财务报表
 */
@Data
public class GameFinancialReportVO implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "统计日期（年月）")
    private String statisticsTime;

    @ApiModelProperty(value = "租户标识")
    private String customerCode;

    @ApiModelProperty(value = "游戏大类编码")
    private String gameType;

    @ApiModelProperty(value = "游戏大类名字")
    private String gameTypeName;

    @ApiModelProperty(value = "游戏大类Id")
    private Integer gameTypeId;

    @ApiModelProperty(value = "游戏平台编码")
    private String platformCode;

    @ApiModelProperty(value = "游戏平台名字")
    private String platformName;

    @ApiModelProperty(value = "一级游戏编码")
    private String gameKind;

    @ApiModelProperty(value = "一级游戏名称")
    private String gameName;

    @ApiModelProperty(value = "有效投注额")
    private BigDecimal validAmount;

    @ApiModelProperty(value = "输赢金额")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "累计输赢金额")
    private BigDecimal accumulateWinAmount;

    @ApiModelProperty(value = "统计开始时间")
    private String startTime;

    @ApiModelProperty(value = "统计结束时间")
    private String endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
