package com.gameplat.admin.model.domain.proxy;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 分红统计汇总
 * @Author : cc
 * @Date : 2022/2/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@TableName("divide_summary")
public class DivideSummary {
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "分红期数表主键ID")
    private Long periodsId;

    @ApiModelProperty(value = "分红代理的用户主键id")
    private Long userId;

    @ApiModelProperty(value = "分红代理的名称")
    private String account;

    @ApiModelProperty(value = "分红代理的层级")
    private Integer agentLevel;

    @ApiModelProperty(value = "分红代理上级的用户id")
    private Long parentId;

    @ApiModelProperty(value = "分红代理的上级")
    private String parentName;

    @ApiModelProperty(value = "分红代理的代理路径")
    private String agentPath;

    @ApiModelProperty(value = "汇总状态 1 已结算  2 已派发  3 部分派发(预留状态)")
    private Integer status;

    @ApiModelProperty(value = "所有下级总的有效投注")
    private BigDecimal validAmount;

    @ApiModelProperty(value = "所有下级总的输赢金额")
    private BigDecimal winAmount;

    @ApiModelProperty(value = "总分红金额")
    private BigDecimal divideAmount;

    @ApiModelProperty(value = "真实总分红金额")
    private BigDecimal realDivideAmount;

    @ApiModelProperty(value = "上期累计金额")
    private BigDecimal lastPeriodsAmount;

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
