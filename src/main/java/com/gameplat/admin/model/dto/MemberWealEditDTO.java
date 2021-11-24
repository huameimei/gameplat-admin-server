package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 修改福利入参
 * @date 2021/11/22
 */

@Data
public class MemberWealEditDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "福利名称")
    private String name;

    @ApiModelProperty(value = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
    private Integer type;

    @ApiModelProperty(value = "周期  开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date startDate;

    @ApiModelProperty(value = "周期  结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date endDate;

    @ApiModelProperty(value = "最低充值金额")
    private BigDecimal minRechargeAmount;

    @ApiModelProperty(value = "最低有效投注金额")
    private BigDecimal minBetAmount;

    @ApiModelProperty(value = "备注")
    private String remark;
}
