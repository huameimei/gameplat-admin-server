package com.gameplat.admin.model.bean;

import com.gameplat.admin.enums.limit.RiskControllerTypeEnum;
import com.gameplat.common.exception.ServiceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import com.gameplat.common.json.JsonUtils;

/** 转换类 */
public class ChannelLimitsBean {

  /** 通道金额设置标识，0位禁用 */
  private Integer limitStatus;

  /** 通道金额收款上限 */
  private Long limitAmount;

  /** 通道时间设置标识，0为启用时间设置，1位禁用时间设置 */
  private Integer channelTimeStatus;

  /** 通道显示开始时间 */
  private Integer channelTimeStart;

  /** 通道显示结束时间 */
  private Integer channelTimeEnd;

  /** 通道展示端，1展示在电脑，2展示在安卓，3展示在IOS */
  private String channelShows;

  /** 通道单笔金额金额最小值 */
  private Long minAmountPerOrder;

  /** 通道单笔金额金额最小值 */
  private Long maxAmountPerOrder;

  /** 通道风控金额类型 0.任何金额 1.浮动金额 2.固定金额 3浮动固定金额 */
  private Integer riskControlType;

  /** 风控值 */
  private String riskControlValue;

  /** 虚拟货币汇率 */
  private String currencyType;

  public ChannelLimitsBean() {}

  public ChannelLimitsBean(
      Integer limitStatus,
      Long limitAmount,
      Integer channelTimeStatus,
      Integer channelTimeStart,
      Integer channelTimeEnd,
      String channelShows,
      Long minAmountPerOrder,
      Long maxAmountPerOrder,
      Integer riskControlType,
      String riskControlValue,
      String currencyType) {
    this.limitStatus = limitStatus;
    this.limitAmount = limitAmount;
    this.channelTimeStatus = channelTimeStatus;
    this.channelTimeStart = channelTimeStart;
    this.channelTimeEnd = channelTimeEnd;
    this.channelShows = channelShows;
    this.minAmountPerOrder = minAmountPerOrder;
    this.maxAmountPerOrder = maxAmountPerOrder;
    this.riskControlType = riskControlType;
    this.riskControlValue = riskControlValue;
    this.currencyType = currencyType;
  }

  public Integer getLimitStatus() {
    if (null == limitStatus) {
      return 0;
    }
    return limitStatus;
  }

  public void setLimitStatus(Integer limitStatus) {
    this.limitStatus = limitStatus;
  }

  public Long getLimitAmount() {
    if (null == limitAmount) {
      return 0L;
    }
    return limitAmount;
  }

  public void setLimitAmount(Long limitAmount) {
    this.limitAmount = limitAmount;
  }

  public Integer getChannelTimeStatus() {
    if (null == channelTimeStatus) {
      return 1;
    }
    return channelTimeStatus;
  }

  public void setChannelTimeStatus(Integer channelTimeStatus) {
    this.channelTimeStatus = channelTimeStatus;
  }

  public Integer getChannelTimeStart() {
    if (null == channelTimeStart) {
      return 0;
    }
    return channelTimeStart;
  }

  public void setChannelTimeStart(Integer channelTimeStart) {
    this.channelTimeStart = channelTimeStart;
  }

  public Integer getChannelTimeEnd() {
    if (null == channelTimeEnd) {
      return 24;
    }
    return channelTimeEnd;
  }

  public void setChannelTimeEnd(Integer channelTimeEnd) {
    this.channelTimeEnd = channelTimeEnd;
  }

  public String getChannelShows() {
    if (StringUtils.isBlank(channelShows)) {
      return "";
    }
    return channelShows;
  }

  public void setChannelShows(String channelShows) {
    this.channelShows = channelShows;
  }

  public Long getMinAmountPerOrder() {
    if (null == minAmountPerOrder) {
      return 0L;
    }
    return minAmountPerOrder;
  }

  public void setMinAmountPerOrder(Long minAmountPerOrder) {
    this.minAmountPerOrder = minAmountPerOrder;
  }

  public Long getMaxAmountPerOrder() {
    if (null == maxAmountPerOrder) {
      return 99999L;
    }
    return maxAmountPerOrder;
  }

  public void setMaxAmountPerOrder(Long maxAmountPerOrder) {
    this.maxAmountPerOrder = maxAmountPerOrder;
  }

  public Integer getRiskControlType() {
    if (null == riskControlType) {
      return 0;
    }
    return riskControlType;
  }

  public void setRiskControlType(Integer riskControlType) {
    this.riskControlType = riskControlType;
  }

  public String getRiskControlValue() {
    if (StringUtils.isBlank(riskControlValue)) {
      return "";
    }
    return riskControlValue;
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

  public static ChannelLimitsBean conver2Bean(String beanStr) {
    if (StringUtils.isBlank(beanStr)) {
      return new ChannelLimitsBean();
    }
    return JsonUtils.toObject(beanStr, ChannelLimitsBean.class);
  }

  /**
   * 检验风控金额的合法性
   *
   * @param riskControlValue
   * @return
   */
  public static String validateRiskControlValue(String riskControlValue, Integer riskControlType) {
    /** 随意金额风控值校验 */
    if (RiskControllerTypeEnum.ANY_AMOUNT.getType() == riskControlType) {
      return "";
    }
    if (RiskControllerTypeEnum.FLOAT_AMOUNT.getType() == riskControlType) {
      if (StringUtils.isBlank(riskControlValue)) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      Pattern p = Pattern.compile("^\\d-\\d$");
      Matcher m = p.matcher(riskControlValue);
      if (!(m.matches() && isCompaire(riskControlValue))) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      return riskControlValue;
    } else if (RiskControllerTypeEnum.FIXED_ACCOUNT.getType() == riskControlType) {
      if (StringUtils.isBlank(riskControlValue)) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      Pattern p = Pattern.compile("^\\d[\\d|.]+\\d$");
      Matcher m = p.matcher(riskControlValue);
      boolean result = m.matches();
      if (!result) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      return rebuildValues(riskControlValue);
    } else {
      if (StringUtils.isBlank(riskControlValue)) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      Pattern p = Pattern.compile("^[\\d-|@]+$");
      Matcher m = p.matcher(riskControlValue);

      if (!(m.matches() && isCompaire(riskControlValue))) {
        throw new ServiceException("风控金额值配置有误，请检查");
      }
      String[] arrays = riskControlValue.split("@");
      return arrays[0] + "@" + rebuildValues(arrays[1]);
    }
  }

  private static String rebuildValues(String values) throws ServiceException {
    String[] arrays = values.split("\\|");
    StringBuffer resultBuff = new StringBuffer();
    for (int i = 0; i < arrays.length; i++) {

      if (StringUtils.isNotBlank(arrays[i])) {
        /** 允许有小数点 */
        String[] moneys = arrays[i].split(".");
        if (moneys.length > 2) {
          throw new ServiceException("风控金额值配置有误");
        }
        resultBuff.append(arrays[i]).append("|");
      }
    }
    if (StringUtils.isBlank(resultBuff.toString())) {
      throw new ServiceException("风控金额值配置有误");
    }
    return resultBuff.deleteCharAt(resultBuff.lastIndexOf("|")).toString();
  }

  private static boolean isCompaire(String riskControlValue) {
    String[] values = riskControlValue.split("@")[0].split("-");
    Integer min = Integer.valueOf(values[0]) * 100;
    Integer max = Integer.valueOf(values[1]) * 100;
    return max > min;
  }

  /** 选择随机加减开关 */
  public static Integer selectAddOrSubSwitch() {
    Integer[] symbol = {0, 1};
    Integer number = symbol[(int) (0 + Math.random() * (symbol.length - 1 + 1))];
    return number;
  }
}
