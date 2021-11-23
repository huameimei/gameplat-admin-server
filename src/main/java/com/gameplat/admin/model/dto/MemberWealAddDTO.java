package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 会员等级福利入参
 * @date 2021/11/22
 */

@Data
public class MemberWealAddDTO implements Serializable {

    @ApiModelProperty(value = "福利名称")
    private String name;

    @ApiModelProperty(value = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
    private Integer type;

    @ApiModelProperty(value = "周期  开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty(value = "周期  结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(value = "最低充值金额")
    private BigDecimal minRechargeAmount;

    @ApiModelProperty(value = "最低有效投注金额")
    private BigDecimal minBetAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "0:未结算  1：未派发   2：已派发  3：已回收")
    private Integer status;
}
