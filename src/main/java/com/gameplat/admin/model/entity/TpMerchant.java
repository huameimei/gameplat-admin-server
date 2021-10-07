package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tp_merchant")
public class TpMerchant extends BaseEntity<TpMerchant> {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "第三方接口编码")
    private String tpInterfaceCode;

    @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
    private Integer status;

    @ApiModelProperty(value = "充值次数")
    private Long rechargeTimes;

    @ApiModelProperty(value = "充值金额")
    private Long rechargeAmount;

    @ApiModelProperty(value = "商户参数JSON")
    private String parameters;

    @ApiModelProperty(value = "第三方开通渠道JSON")
    private String payTypes;
}
