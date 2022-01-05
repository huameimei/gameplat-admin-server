package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员出入款统计表
 * @date 2022/1/5
 */
@Data
@TableName("member_rw_report")
public class MemberRwReport implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主健ID")
    private Long id;

    @ApiModelProperty(value = "会员ID")
    private Integer memberId;

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "会员所属的上级路径")
    private String superPath;

    @ApiModelProperty(value = "报表时间yyyy-mm-dd")
    private Date statTime;

    @ApiModelProperty(value = "添加时间")
    private String addTime;

    @ApiModelProperty(value = "会员当天出款总额")
    private Double withdrawMoney;

    @ApiModelProperty(value = "会员当天出款总次数")
    private Integer withdrawCount;

    @ApiModelProperty(value = "出款手续费")
    private Double counterFee;

    @ApiModelProperty(value = "人工当天出款的总额")
    private Double handWithdrawMoney;

    @ApiModelProperty(value = "人工当天出款的总次数")
    private Integer handWithdrawCount;

    @ApiModelProperty(value = "首次出款总金额")
    private Double firstWithdrawMoney;

    @ApiModelProperty(value = "首次出款类型1会员取款2人工取款")
    private Integer firstWithdrawType;

    @ApiModelProperty(value = "员当天银行入款总额")
    private Double bankMoney;

    @ApiModelProperty(value = "员当天银行入款总次数")
    private Integer bankCount;

    @ApiModelProperty(value = "员当天在线入款总额")
    private Double onlineMoney;

    @ApiModelProperty(value = "会员当天在线入款总次数")
    private Integer onlineCount;

    @ApiModelProperty(value = "人工当天存款总额")
    private Double handRechMoney;

    @ApiModelProperty(value = "人工當天存款总次数")
    private Integer handRechCount;

    @ApiModelProperty(value = "当天入款优惠总金额(包含銀行存款优惠，第三方充值优惠)")
    private Double rechDiscount;

    @ApiModelProperty(value = "当天其它优惠金额(非存款优惠)")
    private Double otherDiscount;

    @ApiModelProperty(value = "首次存款总金额")
    private Double firstRechMoney;

    @ApiModelProperty(value = "首次存款的类型1银行充值2在线充值3人工充值")
    private Integer firstRechType;

    @ApiModelProperty(value = "大股东用户的账号")
    private String dgdAccount;

    @ApiModelProperty(value = "股东用户账号")
    private String gdAccount;

    @ApiModelProperty(value = "总代理用户的账号")
    private String zdlAccount;

    @ApiModelProperty(value = "代理用户的账号")
    private String dlAccount;

    @ApiModelProperty(value = "非正常入款金额")
    private Double exceptionRechargeAmount;

    @ApiModelProperty(value = "非正常出款金额")
    private Double exceptionWithdrawAmount;

    @ApiModelProperty(value = "会员当天第三方出款总额")
    private Double thirdWithdrawMoney;

    @ApiModelProperty(value = "会员当天第三方出款总次数")
    private Integer thirdWithdrawCount;

    @ApiModelProperty(value = "虚拟币充值金额")
    private Double virtualRechargeMoney;

    @ApiModelProperty(value = "虚拟币充值数量")
    private Double virtualRechargeNumber;

    @ApiModelProperty(value = "虚拟币提现金额")
    private Double virtualWithdrawMoney;

    @ApiModelProperty(value = "虚拟币提现数量")
    private Double virtualWithdrawNumber;
}
