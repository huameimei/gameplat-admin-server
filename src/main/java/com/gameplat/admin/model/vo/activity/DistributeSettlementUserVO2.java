package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: lyq
 * @Date: 2020/8/26 17:57
 * @Description:
 */
@Data
public class DistributeSettlementUserVO2 implements Serializable {

    private static final long serialVersionUID = 3747096353561552312L;

    private Long userId;

    @ApiModelProperty(value = "会员账号")
    private String username;

    private BigDecimal money;

    @ApiModelProperty(value = "提现打码量")
    private BigDecimal withdrawDml;

    @ApiModelProperty(value = "与资格管理关联id")
    private String qualificationActivityId;

    @ApiModelProperty(value = "派发id")
    private Long distributeId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "领取方式")
    private Integer getWay;

    private String createBy;
}
