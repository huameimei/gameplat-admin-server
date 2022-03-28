package com.gameplat.admin.model.vo;

import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpreadLinkInfoVo {

  Map<String, List<GameDivideVo>> ownerConfigMap;
  private Long id;
  /** 代理账号 */
  private String agentAccount;
  /** 推广码 */
  private String code;
  /** 推广地址 */
  private String externalUrl;
  /** 推广类型 */
  private Integer spreadType;
  /** 是否专属类型 */
  private Integer exclusiveFlag;
  /** 推广用户类型 */
  private Integer userType;
  /** 用户层级 */
  private Integer userLevel;
  /** 有效天数 */
  private Integer effectiveDays;
  /** 访问数 */
  private Integer visitCount;
  /** 注册数 */
  private Integer registCount;
  /** 彩票反水比率 */
  private double rebate;
  /** 注册送彩金 */
  private BigDecimal discountAmount;
  /** 状态 */
  private Integer status;

  private Integer isOpenDividePreset;
  private String divideConfig;
  private String createBy;
  /** 创建时间 */
  private Date createTime;
  /** 更新者 */
  private String updateBy;
  /** 更新时间 */
  private Date updateTime;

  private String remark;
  private String defaultDevideConfig;
  private String superPath;
  private Integer agentLevel;
  private Map<String, JSONObject> divideMap;
}
