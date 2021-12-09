package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 查询现金流水出参
 * @date 2021/12/2
 */

@Data
public class MemberBillVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户编号")
    private Long memberId;

    @ApiModelProperty(value = "账号")
    @Excel(name = "会员账号", height = 20, width = 30, isImportField = "true_st")
    private String account;

    @ApiModelProperty(value = "交易类型：TranTypes中值")
    @Excel(name = "账变类型", height = 20, width = 30, isImportField = "true_st")
    private Integer tranType;

    @ApiModelProperty(value = "分表ID")
    private Integer tableIndex;

    @ApiModelProperty(value = "订单号，关联其他业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "交易金额")
    @Excel(name = "账变金额", height = 20, width = 30, isImportField = "true_st")
    private BigDecimal amount;

    @ApiModelProperty(value = "账变前的余额")
    @Excel(name = "账变前的余额", height = 20, width = 30, isImportField = "true_st")
    private BigDecimal balance;

    @ApiModelProperty(value = "账变内容")
    @Excel(name = "账变内容", height = 20, width = 30, isImportField = "true_st")
    private String content;

    @ApiModelProperty(value = "操作人")
    @Excel(name = "操作人", height = 20, width = 30, isImportField = "true_st")
    private String operator;

    @ApiModelProperty(value = "添加时间")
    @Excel(name = "账变时间", height = 20, width = 30, isImportField = "true_st")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;
}
