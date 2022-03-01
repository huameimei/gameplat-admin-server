package com.gameplat.admin.model.domain.proxy;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 分红统计详情
 * @Author : cc
 * @Date : 2022/2/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@TableName("divide_detail")
public class DivideDetail {
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分红期数表主键ID")
    private Long periodsId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "分红代理的用户主键id")
    private Long proxyId;

    @ApiModelProperty(value = "分红代理的名称")
    private String proxyName;

    @ApiModelProperty(value = "分红代理的层级")
    private Integer agentLevel;

    @ApiModelProperty(value = "分红代理的代理路径")
    private String superPath;

    @ApiModelProperty(value = "游戏大类code")
    private String liveCode;

    @ApiModelProperty(value = "游戏大类name")
    private String liveName;

    @ApiModelProperty(value = "一级游戏code")
    private String code;

    @ApiModelProperty(value = "一级游戏name")
    private String name;

    @ApiModelProperty(value = "此用户的有效投注")
    private BigDecimal validAmount;

    @ApiModelProperty(value = "此用户的输赢金额")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "结算方式 1 输赢金额 2  有效投注")
    private Integer settleType;

    @ApiModelProperty(value = "金额比例")
    private BigDecimal amountRatio;

    @ApiModelProperty(value = "分红代理所在此条代理线的分红比例")
    private BigDecimal divideRatio;

    @ApiModelProperty(value = "分红金额")
    private BigDecimal divideAmount;

    @ApiModelProperty(value = "分红公式")
    private String divideFormula;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
