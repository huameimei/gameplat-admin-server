package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GameBizDTO implements Serializable {
  /** 平台编码 */
  private String platformCode;

  /** 游戏大类 */
  private String gameType;

  /** 进游戏编码 */
  private String playCode;

  /** IP地址 */
  private String ipAddress;

  /** 是否是手机端 */
  private Boolean isMobile;

  /** 请求域名 */
  private String baseUrl;

  /** 游戏配置信息对象 */
  private Object config;

  /** 游戏账号 */
  private String gameAccount;

  /** 是否是网页端 */
  private Boolean isWeb;

  /** 转换方式 */
  private Boolean transferType;

  /** 额度换入的平台编码 从哪里来 */
  private String transferIn;

  /** 额度转出的平台编码 到哪里去 */
  private String transferOut;

  private String orderNo;

  private BigDecimal amount;

  private Object[] params;
}
