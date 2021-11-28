package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 打码量表
 * @Author : lily
 * @Date : 2021/11/27
 */
@Data
@TableName("valid_withdraw")
public class ValidWithdraw implements Serializable {

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long memberId;

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "充值记录ID(来源id)")
    private String rechId;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal rechMoney;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "类型： 0表示最新一笔充值1表示其它")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "常态打码量")
    private BigDecimal mormDml;

    @ApiModelProperty(value = "优惠打码量")
    private BigDecimal discountDml;

    @ApiModelProperty(value = "彩票打码量")
    private BigDecimal cpDml;

    @ApiModelProperty(value = "体育打码量")
    private BigDecimal sportsDml;

    @ApiModelProperty(value = "真人打码量")
    private BigDecimal videoDml;

    @ApiModelProperty(value = "状态: 0正常,1表示已出款")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
