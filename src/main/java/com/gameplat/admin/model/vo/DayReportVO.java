package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员日报表出参
 * @date 2021/12/31
 */
@Data
public class DayReportVO implements Serializable {

    private static final long serialVersionUID = -3196207776088644481L;

    @ApiModelProperty(value = "统计日期")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date statTime;

    @ApiModelProperty(value = "转账汇款金额")
    private Double bankMoney;

    @ApiModelProperty(value = "转账汇款次数")
    private Integer bankCount;

    @ApiModelProperty(value = "在线支付金额")
    private Double onlineMoney;

    @ApiModelProperty(value = "在线支付次数")
    private Integer onlineCount;

    @ApiModelProperty(value = "人工充值金额")
    private Double handRechMoney;

    @ApiModelProperty(value = "人工充值次数")
    private Integer handRechCount;

    @ApiModelProperty(value = "充值汇总金额")
    private Double totalRechargeAmount;

    @ApiModelProperty(value = "充值汇总次数")
    private Integer totalRechargeCount;

    @ApiModelProperty(value = "充值优惠")
    private Double rechDiscount;

    @ApiModelProperty(value = "其他优惠")
    private Double otherDiscount;

    @ApiModelProperty(value = "非正常入款")
    private Double exceptionRechargeAmount;

    @ApiModelProperty(value = "会员取款金额")
    private Double totalWithdrawAmount;

    @ApiModelProperty(value = "会员取款次数")
    private Integer totalWithdrawCount;

    @ApiModelProperty(value = "人工提现金额")
    private Double handWithdrawMoney;

    @ApiModelProperty(value = "人工提现次数")
    private Integer handWithdrawCount;

    @ApiModelProperty(value = "提现汇总金额")
    private Double withdrawMoney;

    @ApiModelProperty(value = "提现汇总次数")
    private Integer withdrawCount;

    @ApiModelProperty(value = "手续费")
    private Double counterFee;

    @ApiModelProperty(value = "非正常出款")
    private Double exceptionWithdrawAmount;

    @ApiModelProperty(value = "彩票投注额")
    private Double cpBetMoney;

    @ApiModelProperty(value = "彩票输赢")
    private Double cpWinMoney;

    @ApiModelProperty(value = "体育投注额")
    private Double spBetMoney;

    @ApiModelProperty(value = "体育输赢")
    private Double spWinMoney;

    @ApiModelProperty(value = "真人投注额")
    private Double liveBetMoney;

    @ApiModelProperty(value = "真人输赢")
    private Double liveWinMoney;

    @ApiModelProperty(value = "真人返水")
    private Double liveRebateMoney;

    @ApiModelProperty(value = "游戏输赢汇总")
    private Double totalWinOrcloseMoney;

    @ApiModelProperty(value = "代理返点")
    private Double dlRebateMoney;

    @ApiModelProperty(value = "代理分红")
    private Double dlBonuus;

    @ApiModelProperty(value = "代理日工资")
    private Double dlDayWage;

    @ApiModelProperty(value = "层层代理分红")
    private Double dlRatio;

    @ApiModelProperty(value = "团队分红")
    private Double teamWage;

    @ApiModelProperty(value = "出入款差")
    private Double inAndOut;

    @ApiModelProperty(value = "公司收入汇总")
    private Double totalRemainAmount;
}
