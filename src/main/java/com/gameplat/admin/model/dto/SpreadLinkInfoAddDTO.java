package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.vo.GameDivideVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class SpreadLinkInfoAddDTO {

  Map<String, List<GameDivideVo>> ownerConfigMap;
  /** 代理Id */
  private Long agentId;
  /** 代理账号 */
  private String agentAccount;
  /** 推广码 */
  private String code;
  /** 推广地址 */
  private String externalUrl;
  /** 推广类型 */
  private Integer spreadType;
  /** 推广用户类型 */
  private Integer userType;
  /** 2 默认推广域名 1 专属域名 0 公共域名 */
  private Integer exclusiveFlag;
  /** 用户层级 */
  private Integer userLevel;
  /** 彩票反水比率 */
  private double rebate;
  /** 有效天数 */
  private Integer effectiveDays;
  /** 注册送彩金 */
  private BigDecimal discountAmount;

  private Integer isOpenDividePreset;

  /**
   * 专属域名地址
   */
  private String sourceDomain;
}
