package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "是否开启VIP 1：开启   0：关闭")
  private Integer isEnableVip;

  @Schema(description = "是否开启充值 增加成长值 策略 1：开启   0：关闭")
  private Integer isEnableRecharge;

  @Schema(description = "是否开启签到 增加成长值 策略 1：开启   0：关闭")
  private Integer isEnableSign;

  @Schema(description = "是否开启 打码量增加 增长成长值 策略")
  private Integer isEnableDama;

  @Schema(description = "是否发放升级奖励")
  private Integer isPayUpReword;

  @Schema(description = "是否重复发放升级奖励")
  private Integer isRepeatPayUpReword;

  @Schema(description = "是否自动派发升级奖励:1：是  0：否")
  private Integer isAutoPayReword;

  @Schema(description = "每日签到奖励成长值")
  private Long signEveryDayGrowth;

  @Schema(description = "单个会员总的签到最大成长值")
  private Long signMaxGrowth;

  @Schema(description = "每日签到最少充值金额")
  private BigDecimal signDayMinRechargeAmount;

  @Schema(description = "单个会员至少充值此金额才能签到增长成长值")
  private BigDecimal signMinRechargeAmount;

  @Schema(description = "每日签到的IP限制")
  private Integer signIpLimitCount;

  @Schema(description = "充值金额成长值兑换比例")
  private BigDecimal rechageRate;

  @Schema(description = "打码量兑换成长值比例")
  private BigDecimal damaRate;

  @Schema(description = "绑定银行卡奖励成长值")
  private Long bindBankGrowth;

  @Schema(description = "完善用户资料奖励成长值")
  private Long perfectUserInfoGrowth;

  @Schema(description = "所需完善用户的资料列")
  private String perfectUserInfoField;

  @Schema(description = "保级周期")
  private Integer demoteCycle;

  @Schema(description = "最高等级")
  private Integer limitLevel;

  @Schema(description = "未领取失效周期")
  private Integer receiveLimitCycle;

  @Schema(description = "轮播图  多张用,分割")
  private String carousel;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "VIP经验值描述")
  private String growthDesc;

  @Schema(description = "其它提示")
  private String otherDesc;

  @Schema(description = "VIP经验值描述 en-US")
  private String growthDescEn;

  @Schema(description = "VIP经验值描述 th-TH")
  private String growthDescTh;

  @Schema(description = "VIP经验值描述 in-ID")
  private String growthDescIn;

  @Schema(description = "VIP经验值描述 vi-VN")
  private String growthDescVi;

  @Schema(description = "其它提示 en-US")
  private String otherDescEn;

  @Schema(description = "其它提示 th-TH")
  private String otherDescTh;

  @Schema(description = "其它提示 in-ID")
  private String otherDescIn;

  @Schema(description = "其它提示 vi-VN")
  private String otherDescVi;

  @Schema(description = "金币成长值比例")
  private Double coinRatio;

  @Schema(description = "金币成长值速率")
  private Double coinRate;

  @Schema(description = "金币描述")
  private String goldCoinDesc;

  @Schema(description = "是否开启借呗")
  private Integer isMemberLoan;

  @Schema(description = "是否开启借款")
  private Integer isLoanMoney;

  @Schema(description = "单日借款上限")
  private Integer dayLendLimit;

  @Schema(description = "单日还款上限")
  private Integer dayReturnLimit;

  @Schema(description = "单次最低还款金额")
  private BigDecimal lowerMoney;
}
