package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**  提现配置*/
@Data
@TableName("sys_cash_config")
public class SysCashConfig implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @ApiModelProperty(value = "提现比例 1提现币等于n人民币")
    private BigDecimal rate;

    @ApiModelProperty(value = "提现类型（1日结 2周结 3月结）")
    private Integer cycleType;

    @ApiModelProperty(value = "提现货币需要大于n才能提现")
    private BigDecimal withdrawNeedNum;

    @ApiModelProperty(value = "提现货币需要大于n才能提现开关 0关闭 1开启")
    private Integer withdrawNeedLimit;

    @ApiModelProperty(value = "单笔提现最低限额")
    private BigDecimal withdrawOneAmount;

    @ApiModelProperty(value = "单笔提现最低限额开关 0关闭 1开启")
    private Integer withdrawOneOpen;

    @ApiModelProperty(value = "单笔提现最高限额")
    private BigDecimal withdrawOneMaxAmount;

    @ApiModelProperty(value = "单笔提现最高限额开关 0关闭 1开启")
    private Integer oneMaxAmountOpen;

    @ApiModelProperty(value = "提现次数")
    private BigDecimal withdrawCount;

    @ApiModelProperty(value = "提现次数开关 0关闭 1开启")
    private Integer withdrawCountOpen;

    @ApiModelProperty(value = "单日提现最大限额")
    private BigDecimal withdrawMaxAmount;

    @ApiModelProperty(value = "单日剩余限额")
    private BigDecimal withdrawtRanslateAmount;


    @ApiModelProperty(value = "单日提现最大限额开关 0关闭 1开启")
    private Integer maxAmountOpen;

    @ApiModelProperty(value = "是否需要实名认证 0不需要 1需要")
    private Integer needReal;

    @ApiModelProperty(value = "是否需要提现银行卡 0不需要 1需要")
    private Integer needBankCard;

    @ApiModelProperty(value = "是否需要已开通直播 0不需要  1需要")
    private Integer needOpenLive;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 是否重复提交
     */
    private String isRepeat;

    /**
     * usdt开关
     */
    @ApiModelProperty("usdt开关  0 关闭  1开启")
    private String usdtCode;

    /**
     * 银行卡开关
     */
    @ApiModelProperty("usdt开关  0 关闭  1开启")
    private String bankCode;

    /**
     * 银行卡开关
     */
    @ApiModelProperty("银行卡解绑开关  0 关闭  1开启")
    private String bankSwith;

    /**
     * usdt解绑开关
     */
    @ApiModelProperty("usdt解绑开关  0 关闭  1开启")
    private String usdtSwith;

}
