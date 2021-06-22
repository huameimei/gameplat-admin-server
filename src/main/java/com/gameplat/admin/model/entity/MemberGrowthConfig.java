package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * 用户成长值配置
 * @author Lenovo
 */
@Data
public class MemberGrowthConfig extends BaseEntity {

  /** 是否开启VIP 0 关闭 1 开启 */
  private Integer isEnableVip;

  /** 是否开启充值增加成长值策略 0 关闭 1 开启 */
  private Integer isEnableRecharge;

  /** 是否开启签到增加成长值策略 0 关闭 1 开启 */
  private Integer isEnableSign;

  /** 是否开启 打码量增加 增长成长值策略 0 关闭 1 开启 */
  private Integer isEnableDml;

  /** 是否发放升级奖励 0 关闭 1 开启 */
  private Integer isPayUpReward;

  /** 是否自动发放升级奖励 0 关闭 1 开启 */
  private Integer isAutoPayReward;

  /** 每日签到奖励成长值 */
  private Integer signEveryDayGrowth;

  /** 单个会员总的签到最大成长值 */
  private Integer signMaxGrowth;

  /** 每天签到最少充值金额 */
  private Long signDayMinRechargeAmount;

  /** 单个会员至少充值此金额才能签到增长成长值 */
  private Integer signMinRechargeAmount;

  /** 每日签到的IP限制 同一个会员不同账号签到不能超过五个 */
  private Integer signIpLimitCount;

  /** 充值金额兑换成长值比例 */
  private Integer rechageRate;

  /** 打码量兑换成长值比例 */
  private Integer dmlRate;

  /** 绑定银行卡奖励成长值 */
  private Integer bindBankGrowth;

  /** 完善用户资料奖励成长值 */
  private Integer perfectUserInfoGrowth;

  /** 所需完善用户的资料列 */
  private String perfectUserInfoField;

  /** 成长等级保级周期 */
  private Integer demoteCycle;

  /** 未领取失效时间 */
  private Integer receiveLimitCycle;

  /** 最高等级 */
  private Integer limitLevel;

  /** VIP经验值描述 */
  private String growthDesc;

  private String  growthDescEn;

  private String growthDescTh;

  private String growthDescIn;

  private String growthDescVi;


  /** 其它提示 */
  private String otherDesc;

  private String otherDescEn;

  private String otherDescTh;

  private String otherDescIn;

  /** 备注 */
  private String remark;


}
