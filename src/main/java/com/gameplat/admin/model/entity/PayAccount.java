package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_account")
public class PayAccount extends BaseEntity<PayAccount> {

    private String name;

    private String account;

    private String owner;

    private String payType;

    private String bankName;

    private String bankAddress;

    private String qrCode;

    private String userLevels;

    private String remarks;

    private Integer sort;

    @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
    private Integer status;

    private Long rechargeTimes;

    private Long rechargeAmount;

    private String orderRemark;

    @ApiModelProperty(value = "0:启用，1：关闭")
    private Integer orderRemarkStatus;

    private String limitInfo;

    private String handleTip;
}
