package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 借呗
 * @date 2022/3/6
 */
@Data
public class MemberLoanQueryDTO implements Serializable {

    @ApiModelProperty("会员账号")
    private String account;

    @ApiModelProperty("VIP等级")
    private Integer vipLevel;

    @ApiModelProperty("欠款金额")
    private Double overdraftMoney;

    @ApiModelProperty("借款时间")
    private Date loanTime;

    @ApiModelProperty("借款状态  0:成功  1:失败  2:已回收")
    private Integer loanStatus;
}
