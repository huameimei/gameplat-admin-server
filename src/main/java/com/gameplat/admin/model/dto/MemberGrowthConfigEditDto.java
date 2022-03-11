package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "修改VIP配置入参", description = "修改VIP配置入参")
public class MemberGrowthConfigEditDto implements Serializable {

  @ApiModelProperty(value = "主键", name = "id", required = true)
  @NotNull(message = "编号不能为空")
  private Long id;

  @ApiModelProperty(value = "是否开启VIP功能 1：开启   0：关闭", name = "isEnableVip", required = false)
  private Integer isEnableVip;

  @ApiModelProperty(value = "是否开启充值计算 1：开启   0：关闭", name = "isEnableRecharge", required = false)
  private Integer isEnableRecharge;

  @ApiModelProperty(value = "是否开启打码量计算 1：开启   0：关闭", name = "isEnableDama", required = false)
  private Integer isEnableDama;

  @ApiModelProperty(value = "是否开启签到计算 1：开启   0：关闭", name = "isEnableSign", required = false)
  private Integer isEnableSign;

  @ApiModelProperty(value = "开始签到最少充值金额", name = "signMinRechargeAmount", required = false)
  private BigDecimal signMinRechargeAmount;

  @ApiModelProperty(value = "同IP最多签到用户数", name = "signIpLimitCount", required = false)
  private Integer signIpLimitCount;

  @ApiModelProperty(value = "是否发放升级奖励", name = "isPayUpReword", required = false)
  private Integer isPayUpReword;

  @ApiModelProperty(value = "是否自动派发升级奖励:1：是  0：否", name = "isAutoPayReword", required = false)
  private Integer isAutoPayReword;

  @ApiModelProperty(value = "未领取失效周期(天)", name = "receiveLimitCycle", required = false)
  private String receiveLimitCycle;

  @ApiModelProperty(value = "充值金额成长值兑换比例", name = "rechageRate", required = false)
  private BigDecimal rechageRate;

  @ApiModelProperty(value = "打码量兑换成长值比例", name = "damaRate", required = false)
  private BigDecimal damaRate;

  @ApiModelProperty(value = "每日签到奖励成长值", name = "signEveryDayGrowth", required = false)
  private Long signEveryDayGrowth;

  @ApiModelProperty(value = "每日签到奖励成长值", name = "signEveryDayGrowth", required = false)
  private Long bindBankGrowth;

  @ApiModelProperty(value = "每日签到最少充值金额", name = "signDayMinRechargeAmount", required = false)
  private BigDecimal signDayMinRechargeAmount;

  @ApiModelProperty(value = "签到最大成长值", name = "signMaxGrowth", required = false)
  private Long signMaxGrowth;

  @ApiModelProperty(value = "是否重复发放升级奖励", name = "isRepeatPayUpReword", required = false)
  private Integer isRepeatPayUpReword;

  @ApiModelProperty(value = "最高等级配置", name = "limitLevel", required = false)
  private Integer limitLevel;

  @ApiModelProperty(value = "保级周期", name = "demoteCycle", required = false)
  private Integer demoteCycle;

  @ApiModelProperty(value = "更新人", hidden = true)
  private String updateBy;

  @ApiModelProperty(value = "语言", hidden = true)
  private String language;

  @ApiModelProperty("完善用户资料奖励成长值")
  private Long perfectUserInfoGrowth;

  @ApiModelProperty("所需完善用户的资料列")
  private String perfectUserInfoField;

  @ApiModelProperty("VIP经验值描述")
  private String growthDesc;

  @ApiModelProperty("其它提示")
  private String otherDesc;

  @ApiModelProperty("轮播图  多张用,分割")
  private String carousel;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "备注")
  private String remark;

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

  @ApiModelProperty("移动端VIP图片")
  private String mobileVipImage;

  @ApiModelProperty("WEB端VIP图片")
  private String webVipImage;

  @ApiModelProperty("移动端达成背景图片")
  private String mobileReachBackImage;

  @ApiModelProperty("移动端未达成背景图片")
  private String mobileUnreachBackImage;

  @ApiModelProperty("移动端达成VIP图片")
  private String mobileReachVipImage;

  @ApiModelProperty("移动端未达成VIP图片")
  private String mobileUnreachVipImage;

  @ApiModelProperty("WEB端达成VIP图片")
  private String webReachVipImage;

  @ApiModelProperty("WEB端未达成VIP图片")
  private String webUnreachVipImage;

    @ApiModelProperty("金币成长值比例")
    private BigDecimal coinRatio;

    @ApiModelProperty("金币最大获取数")
    private Integer maxCoin;

    @ApiModelProperty("金币最大获取数")
    private Integer dailyMaxCoin;

  @ApiModelProperty("金币成长值速率")
  private Double coinRate;

  @ApiModelProperty("金币描述")
  private String goldCoinDesc;

  @ApiModelProperty("是否开启借呗")
  private String isMemberLoan;

  @ApiModelProperty("是否开启借款")
  private Integer isLoanMoney;

  @ApiModelProperty("单日借款上限")
  private Integer dayLendLimit;

  @ApiModelProperty("单日还款上限")
  private Integer dayReturnLimit;

  @ApiModelProperty("单次最低还款金额")
  private BigDecimal lowerMoney;
}
