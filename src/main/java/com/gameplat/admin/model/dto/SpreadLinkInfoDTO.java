package com.gameplat.admin.model.dto;

import lombok.Data;

@Data
public class SpreadLinkInfoDTO {

  private Long id;
  /** 代理账号 */
  private String agentAccount;
  /** 推广码 */
  private String code;
  /** 推广类型 */
  private Integer spreadType;
  /** 推广用户类型 */
  private Integer userType;
  /** 状态 */
  private Integer status;

  /** 排序列 */
  private String orderByColumn;
  /** 排序 */
  private String sortBy;
}
