package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description vip配置入参
 * @date 2021/11/21
 */
@Data
public class MemberGrowthConfigEditDto implements Serializable {

  @Schema(description = "主键", required = true)
  @NotNull(message = "编号不能为空")
  private Long id;

  @Schema(description = "是否开启VIP功能 1：开启   0：关闭")
  private Integer isEnableVip;

  @Schema(description = "是否开启充值计算 1：开启   0：关闭")
  private Integer isEnableRecharge;

  @Schema(description = "是否开启打码量计算 1：开启   0：关闭")
  private Integer isEnableDama;

  @Schema(description = "是否开启签到计算 1：开启   0：关闭")
  private Integer isEnableSign;

  @Schema(description = "开始签到最少充值金额")
  private BigDecimal signMinRechargeAmount;

  @Schema(description = "同IP最多签到用户数")
  private Integer signIpLimitCount;

  @Schema(description = "是否发放升级奖励")
  private Integer isPayUpReword;

  @Schema(description = "升级奖励打码量")
  private BigDecimal payUpRewordDama;

  @Schema(description = "是否自动派发升级奖励:1：是  0：否")
  private Integer isAutoPayReword;

  @Schema(description = "未领取失效周期(天)")
  private String receiveLimitCycle;

  @Schema(description = "充值金额成长值兑换比例")
  private BigDecimal rechageRate;

  @Schema(description = "打码量兑换成长值比例")
  private BigDecimal damaRate;

  @Schema(description = "每日签到奖励成长值")
  private Long signEveryDayGrowth;

  @Schema(description = "每日签到奖励成长值")
  private Long bindBankGrowth;

  @Schema(description = "每日签到最少充值金额")
  private BigDecimal signDayMinRechargeAmount;

  @Schema(description = "签到最大成长值")
  private Long signMaxGrowth;

  @Schema(description = "是否重复发放升级奖励")
  private Integer isRepeatPayUpReword;

  @Schema(description = "最高等级配置")
  private Integer limitLevel;

  @Schema(description = "保级周期")
  private Integer demoteCycle;

  @Schema(description = "更新人", hidden = true)
  private String updateBy;

  @Schema(description = "语言", hidden = true)
  private String language;

  @Schema(description = "完善用户资料奖励成长值")
  private Long perfectUserInfoGrowth;

  @Schema(description = "所需完善用户的资料列")
  private String perfectUserInfoField;

  @Schema(description = "VIP经验值描述")
  private String growthDesc;

  @Schema(description = "其它提示")
  private String otherDesc;

  @Schema(description = "轮播图  多张用,分割")
  private String carousel;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;

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

  @Schema(description = "移动端VIP图片")
  private String mobileVipImage;

  @Schema(description = "WEB端VIP图片")
  private String webVipImage;

  @Schema(description = "移动端达成背景图片")
  private String mobileReachBackImage;

  @Schema(description = "移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @Schema(description = "移动端达成VIP图片")
  private String mobileReachVipImage;

  @Schema(description = "移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @Schema(description = "WEB端达成VIP图片")
  private String webReachVipImage;

  @Schema(description = "WEB端未达成VIP图片")
  private String webUnreachVipImage;

  @Schema(description = "金币成长值比例")
  private BigDecimal coinRatio;

  @Schema(description = "金币最大获取数")
  private Integer maxCoin;

  @Schema(description = "金币最大获取数")
  private Integer dailyMaxCoin;

  @Schema(description = "金币成长值速率")
  private Double coinRate;

  @Schema(description = "金币描述")
  private String goldCoinDesc;

  @Schema(description = "是否开启借呗")
  private String isMemberLoan;

  @Schema(description = "是否开启借款")
  private Integer isLoanMoney;

  @Schema(description = "单日借款上限")
  private Integer dayLendLimit;

  @Schema(description = "单日还款上限")
  private Integer dayReturnLimit;

  @Schema(description = "单次最低还款金额")
  private BigDecimal lowerMoney;
}
