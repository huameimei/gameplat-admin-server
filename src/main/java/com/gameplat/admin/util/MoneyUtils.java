package com.gameplat.admin.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;


public class MoneyUtils {

  private MoneyUtils() {
  }

  private static final NumberFormat YUAN_FORMATTER = NumberFormat.getInstance();
  private static final NumberFormat CENT_FORMATTER = NumberFormat.getInstance();
  //单位为角
  private static final NumberFormat CORNER_FORMATTER = NumberFormat.getInstance();

  static {
    YUAN_FORMATTER.setGroupingUsed(false); // 防止显示科学计数
    YUAN_FORMATTER.setMaximumFractionDigits(2);// 最大两位小数
    YUAN_FORMATTER.setMinimumFractionDigits(2);// 最小两位小数
    CENT_FORMATTER.setGroupingUsed(false); // 防止显示科学计数
    CENT_FORMATTER.setMaximumFractionDigits(0);// 无小数
    CORNER_FORMATTER.setMaximumFractionDigits(1); //最大一位小数
    CORNER_FORMATTER.setMinimumFractionDigits(1); //最小一位小数
  }

  public static String toYuanStr(BigDecimal amount) {
    return amount != null ? YUAN_FORMATTER.format(amount) : null;
  }

  //元为单位 整数，不带小数部分
  public static String toWholeYuan(BigDecimal amount) {
    return amount != null ? CENT_FORMATTER.format(amount) : null;
  }

  public static String toCentStr(BigDecimal amount) {
    return amount != null ? CENT_FORMATTER.format(amount.multiply(new BigDecimal(100))) : null;
  }

  public static BigDecimal fromYuanStr(String amount) {
    return NumberUtils.toScaledBigDecimal(amount);
  }

  public static BigDecimal fromCentStr(String amount) {
    return NumberUtils.toScaledBigDecimal(amount).divide(new BigDecimal(100));
  }

  //单位为角
  public static String toCornerStr(BigDecimal amount) {
    return amount != null ? CORNER_FORMATTER.format(amount.multiply(new BigDecimal(10))) : null;
  }

  //单位为角
  public static BigDecimal fromCornerStr(String amount) {
    return NumberUtils.toScaledBigDecimal(amount).divide(new BigDecimal(10));
  }

  /**
   * @param scale
   * @param money
   * @return
   */
  public static BigDecimal fix(int scale, BigDecimal money) {
    return money != null ? new BigDecimal(String.valueOf(money))
        .setScale(Math.max(scale, 0), BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
  }

  /*** 向下取整
   * @param scale
   * @param money
   * @return
   */
  public static BigDecimal fixDown(int scale, BigDecimal money) {
    return money != null ? new BigDecimal(String.valueOf(money))
        .setScale(Math.max(scale, 0), BigDecimal.ROUND_DOWN) : BigDecimal.ZERO;
  }
}
