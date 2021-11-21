package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @description vip配置入参
 * @date 2021/11/21
 */

@Data
public class MemberGrowthConfigEditDto implements Serializable {

    @ApiModelProperty("主键")
    @NotNull(message = "编号不能为空")
    private Long id;

    @ApiModelProperty("是否开启VIP 1：开启   0：关闭")
    private Integer isEnableVip;

    @ApiModelProperty("是否开启充值 增加成长值 策略 1：开启   0：关闭")
    private Integer isEnableRecharge;

    @ApiModelProperty("是否开启签到 增加成长值 策略 1：开启   0：关闭")
    private Integer isEnableSign;

    @ApiModelProperty("是否开启 打码量增加 增长成长值 策略")
    private Integer isEnableDama;

    @ApiModelProperty("是否发放升级奖励")
    private Integer isPayUpReword;

    @ApiModelProperty("是否重复发放升级奖励")
    private Integer isRepeatPayUpReword;

    @ApiModelProperty("是否自动派发升级奖励:1：是  0：否")
    private Integer isAutoPayReword;

    @ApiModelProperty("每日签到奖励成长值")
    private Integer signEveryDayGrowth;

    @ApiModelProperty("单个会员总的签到最大成长值")
    private Integer signMaxGrowth;

    @ApiModelProperty("每日签到最少充值金额")
    private BigDecimal signDayMinRechargeAmount;

    @ApiModelProperty("单个会员至少充值此金额才能签到增长成长值")
    private BigDecimal signMinRechargeAmount;

    @ApiModelProperty("每日签到的IP限制")
    private Integer signIpLimitCount;

    @ApiModelProperty("充值金额成长值兑换比例")
    private BigDecimal rechageRate;

    @ApiModelProperty("打码量兑换成长值比例")
    private BigDecimal damaRate;

    @ApiModelProperty("绑定银行卡奖励成长值")
    private Integer bindBankGrowth;

    @ApiModelProperty("完善用户资料奖励成长值")
    private Integer perfectUserInfoGrowth;

    @ApiModelProperty("所需完善用户的资料列")
    private String perfectUserInfoField;

    @ApiModelProperty("保级周期")
    private Integer demoteCycle;

    @ApiModelProperty("最高等级")
    private Integer limitLevel;

    @ApiModelProperty("轮播图  多张用,分割")
    private String carousel;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty("VIP经验值描述")
    private String growthDesc;

    @ApiModelProperty("其它提示")
    private String otherDesc;

    @ApiModelProperty("未领取失效周期")
    private String receiveLimitCycle;

    @ApiModelProperty("语言")
    private String language;
}
