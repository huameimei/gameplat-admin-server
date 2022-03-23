package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.vo.GameDivideVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class SpreadLinkInfoEditDTO {

  Map<String, List<GameDivideVo>> ownerConfigMap;
  private Long id;
  /** 代理账号 */
  private String agentAccount;
  /** 推广地址 */
  private String externalUrl;
  /** 推广码 */
  private String code;
  /** 推广类型 */
  private Integer spreadType;
  /** 推广用户类型 */
  private Integer userType;
  /** 是否专属 1 是 0 否 */
  private Integer exclusiveFlag;
  /** 用户层级 */
  private Integer userLevel;
  /** 彩票反水比率 */
  private double rebate;
  /** 状态 */
  private Integer status;
  /** 有效天数 */
  private Integer effectiveDays;
  /** 注册送彩金 */
  private BigDecimal discountAmount;

  private Integer isOpenDividePreset;
}
