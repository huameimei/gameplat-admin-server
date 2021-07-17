package com.gameplat.admin.model.bean;


import com.gameplat.common.json.JsonUtils;
import jodd.util.StringUtil;

public class PayAccountLimitsBean {

  private Integer bankFlag;

  /**
   * 通道金额设置标识，0位禁用
   */
  private Integer limitStatus;

  /**
   * 通道金额收款上限
   */
  private Double limitAmount;

  private String hyLevel;

  /**
   * 通道时间设置标识，0为启用时间设置，1位禁用时间设置
   */
  private Integer channelTimeStatus;

  /**
   * 通道显示开始时间
   */
  private Integer channelTimeStart;

  /**
   * 通道显示结束时间
   */
  private Integer  channelTimeEnd;

  /**
   * 通道展示端，1展示在电脑，2展示在安卓，3展示在IOS
   */
  private String channelShows;

  /**
   * 通道单笔金额金额最小值
   */
  private Double minAmountPerOrder;

  /**
   * 通道单笔金额金额最小值
   */
  private Double maxAmountPerOrder;

  /**
   * 通道风控金额类型
   * 0.任何金额 1.浮动金额 2.固定金额 3浮动固定金额
   */
  private Integer riskControlType;

  /**
   * 风控值
   */
  private String riskControlValue;

  /**
   * 虚拟货币类型
   */
  private String currencyType;


  public String getHyLevel() {
    return hyLevel;
  }

  public void setHyLevel(String hyLevel) {
    this.hyLevel = hyLevel;
  }

  public Integer getLimitStatus() {
    if(null == limitStatus){
      return 0;
    }
    return limitStatus;
  }

  public void setLimitStatus(Integer limitStatus) {
    this.limitStatus = limitStatus;
  }


  public Integer getBankFlag() {
    return bankFlag;
  }

  public void setBankFlag(Integer bankFlag) {
    this.bankFlag = bankFlag;
  }

  public Integer getChannelTimeStatus() {
    return channelTimeStatus;
  }

  public void setChannelTimeStatus(Integer channelTimeStatus) {
    this.channelTimeStatus = channelTimeStatus;
  }

  public Integer getChannelTimeStart() {
    if(null == channelTimeStart){
      return 0;
    }
    return channelTimeStart;
  }

  public void setChannelTimeStart(Integer channelTimeStart) {
    this.channelTimeStart = channelTimeStart;
  }

  public Integer getChannelTimeEnd() {
    if(null == channelTimeEnd){
      return 24;
    }
    return channelTimeEnd;
  }

  public void setChannelTimeEnd(Integer channelTimeEnd) {
    this.channelTimeEnd = channelTimeEnd;
  }

  public String getChannelShows() {
    if(StringUtil.isBlank(channelShows)){
      return "";
    }
    return channelShows;
  }

  public void setChannelShows(String channelShows) {
    this.channelShows = channelShows;
  }

  public Double getMinAmountPerOrder() {
    if(null == minAmountPerOrder){
      return 0d;
    }
    return minAmountPerOrder;
  }

  public void setMinAmountPerOrder(Double minAmountPerOrder) {
    this.minAmountPerOrder = minAmountPerOrder;
  }

  public Double getMaxAmountPerOrder() {
    if(null == maxAmountPerOrder){
      return 99999d;
    }
    return maxAmountPerOrder;
  }

  public void setMaxAmountPerOrder(Double maxAmountPerOrder) {
    this.maxAmountPerOrder = maxAmountPerOrder;
  }

  public Integer getRiskControlType() {
    if(null == riskControlType){
      return 0;
    }
    return riskControlType;
  }

  public void setRiskControlType(Integer riskControlType) {
    this.riskControlType = riskControlType;
  }

  public String getRiskControlValue() {
    if(StringUtil.isBlank(riskControlValue)){
      return "";
    }
    return riskControlValue;
  }

  public Double getLimitAmount() {
    if(null == limitAmount){
      return 0d;
    }
    return limitAmount;
  }

  public void setLimitAmount(Double limitAmount) {
    this.limitAmount = limitAmount;
  }

  public void setRiskControlValue(String riskControlValue) {
    this.riskControlValue = riskControlValue;
  }

  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
