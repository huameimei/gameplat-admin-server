package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 会员等级福利出参
 * @date 2021/11/22
 */

@Data
public class MemberWealVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "福利名称")
    private String name;

    @ApiModelProperty(value = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
    private Integer type;

    @ApiModelProperty(value = "状态 0:未结算  1：未派发   2：已派发  3：已回收")
    private Integer status;

    @ApiModelProperty(value = "周期  开始时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date startDate;

    @ApiModelProperty(value = "周期  结束时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date endDate;

    @ApiModelProperty(value = "结算时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date settleTime;

    @ApiModelProperty(value = "派发时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date payTime;

    @ApiModelProperty(value = "最低充值金额")
    private BigDecimal minRechargeAmount;

    @ApiModelProperty(value = "最低有效投注金额")
    private BigDecimal minBetAmount;

    @ApiModelProperty(value = "资格会员数")
    private Integer totalUserCount;

    @ApiModelProperty(value = "总的派发金额")
    private BigDecimal totalPayMoney;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;


}
