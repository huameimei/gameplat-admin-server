package com.gameplat.admin.model.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TpPayChannelEditDTO implements Serializable {

  private Long id;

  private String name;

  private Integer merchantId;

  private String tpPayType;

  private String memberLevels;

  private String remarks;

  private String chanDesc;

  private Integer sort;

  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String limitInfo;

  private String payChannelTip;

  /** 通道金额设置标识，0位禁用 */
  private Integer limitStatus;

  /** 通道金额收款上限 */
  private BigDecimal limitAmount;

  /** 通道时间设置标识，0为启用时间设置，1位禁用时间设置 */
  private Integer channelTimeStatus;

  /** 通道显示开始时间 */
  private Integer channelTimeStart;

  /** 通道显示结束时间 */
  private Integer channelTimeEnd;

  /** 通道展示端，1展示在电脑，2展示在安卓，3展示在IOS */
  private String channelShows;

  /** 通道单笔金额金额最小值 */
  private BigDecimal minAmountPerOrder;

  /** 通道单笔金额金额最小值 */
  private BigDecimal maxAmountPerOrder;

  /** 通道风控金额类型 0.任何金额 1.浮动金额 2.固定金额 3浮动固定金额 */
  private Integer riskControlType;

  /** 风控值 */
  private String riskControlValue;

  private Integer typeSubscript;

  private String url;

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
