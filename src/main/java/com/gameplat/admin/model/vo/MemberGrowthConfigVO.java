package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 用户成长等级配置出参
 * @date 2021/11/20
 */
@Data
public class MemberGrowthConfigVO implements Serializable {

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
  private Long signEveryDayGrowth;

  @ApiModelProperty("单个会员总的签到最大成长值")
  private Long signMaxGrowth;

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
  private Long bindBankGrowth;

  @ApiModelProperty("完善用户资料奖励成长值")
  private Long perfectUserInfoGrowth;

  @ApiModelProperty("所需完善用户的资料列")
  private String perfectUserInfoField;

  @ApiModelProperty("保级周期")
  private Integer demoteCycle;

  @ApiModelProperty("最高等级")
  private Integer limitLevel;

  @ApiModelProperty("未领取失效周期")
  private Integer receiveLimitCycle;

  @ApiModelProperty("轮播图  多张用,分割")
  private String carousel;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;

  @ApiModelProperty("VIP经验值描述")
  private String growthDesc;

  @ApiModelProperty("其它提示")
  private String otherDesc;

  @ApiModelProperty("VIP经验值描述 en-US")
  private String growthDescEn;

  @ApiModelProperty("VIP经验值描述 th-TH")
  private String growthDescTh;

  @ApiModelProperty("VIP经验值描述 in-ID")
  private String growthDescIn;

  @ApiModelProperty("VIP经验值描述 vi-VN")
  private String growthDescVi;

  @ApiModelProperty("其它提示 en-US")
  private String otherDescEn;

  @ApiModelProperty("其它提示 th-TH")
  private String otherDescTh;

  @ApiModelProperty("其它提示 in-ID")
  private String otherDescIn;

  @ApiModelProperty("其它提示 vi-VN")
  private String otherDescVi;

  @ApiModelProperty("金币成长值比例")
  private Double coinRatio;

  @ApiModelProperty("金币成长值速率")
  private Double coinRate;

  @ApiModelProperty("金币描述")
  private String goldCoinDesc;

  @ApiModelProperty("是否开启借呗")
  private Integer isMemberLoan;

  @ApiModelProperty("是否开启借款")
  private Integer isLoanMoney;

  @ApiModelProperty("单日借款上限")
  private Integer dayLendLimit;

  @ApiModelProperty("单日还款上限")
  private Integer dayReturnLimit;

  @ApiModelProperty("单次最低还款金额")
  private BigDecimal lowerMoney;
}
