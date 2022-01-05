package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author lily
 * @description 会员综合报表
 * @date 2022/1/4
 */

@Data
@TableName("member_bus_day_report")
public class MemberBusDayReport implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Integer memberId;

    @ApiModelProperty(value = "会员账号")
    private String account;

    @ApiModelProperty(value = "会员真实姓名")
    private String  realName;

    @ApiModelProperty(value = "代理路径")
    private String superPath;

    @ApiModelProperty(value = "会员类型（会员:M 代理：A 推广:P 试玩 :T）")
    private String userType;

    @ApiModelProperty(value = "投注总金额")
    private Double cpBetMoney;

    @ApiModelProperty(value = "中奖总金额")
    private Double cpWinMoney;

    @ApiModelProperty(value = "合局金额")
    private Double cpDrawMoney;

    @ApiModelProperty(value = "返水金额")
    private Double cpRebateMoney;

    @ApiModelProperty(value = "彩票下注笔数")
    private Integer cpBetCount;

    @ApiModelProperty(value = "体育下注金额")
    private Double spBetMoney;

    @ApiModelProperty(value = "体育中奖金额")
    private Double spWinMoney;

    @ApiModelProperty(value = "体育合局金额")
    private Double spDrawMoney;

    @ApiModelProperty(value = "体育返水金额")
    private Double spRebateMoney;

    @ApiModelProperty(value = "体育下注笔数")
    private Integer spBetCount;

    @ApiModelProperty(value = "真人下注金额")
    private Double liveBetMoney;

    @ApiModelProperty(value = "真人有效投注金额")
    private Double liveValidMoney;

    @ApiModelProperty(value = "真人中奖金额")
    private Double liveWinMoney;

    @ApiModelProperty(value = "真人返水金额")
    private Double liveRebateMoney;

    @ApiModelProperty(value = "代理返点金额")
    private Double dlRebateMoney;

    @ApiModelProperty(value = "代理日工资")
    private Double dlDayWage;

    @ApiModelProperty(value = "代理分红")
    private Double dlBonuus;

    @ApiModelProperty(value = "层层代理分红")
    private Double dlRatio;

    @ApiModelProperty(value = "团队分红")
    private Double teamWage;

    @ApiModelProperty(value = "开始时间")
    private Date statTime;

    @ApiModelProperty(value = "添加时间")
    private Timestamp addTime;

    @ApiModelProperty(value = "大股东账号")
    private String dgdAccount;

    @ApiModelProperty(value = "股东账号")
    private String  gdAccount;

    @ApiModelProperty(value = "总代理账号")
    private String zdlAccount;

    @ApiModelProperty(value = "代理账号")
    private String dlAccount;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "月俸禄")
    private Double monthPaid;

    @ApiModelProperty(value = "周俸禄")
    private Double weekPaid;

    @ApiModelProperty(value = "彩金")
    private Double winnings;

    @ApiModelProperty(value = "余额宝收益")
    private Double yubaoInterest;

    @ApiModelProperty(value = "升级奖励")
    private Double upgradeReward;

    @ApiModelProperty(value = "活动彩金")
    private Double promotionBonus;
}
