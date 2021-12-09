package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class OperBizBlacklistDTO implements Serializable {

  private Long id;

  /**
   * 目标类型
   * <ul>
   * <li>0: 会员</li>
   * <li>1: 层级</li>
   * </ul>
   */
  private Integer targetType;
  /**
   * 目标
   * <p>会员帐号、层级值</p>
   */
  private String target;
  /**
   * 黑名单类型
   */
  private String types;
  /**
   * 备注
   */
  private String remark;
  /**
   * 状态
   * <ul>
   * <li>0: 禁用</li>
   * <li>1: 启用</li>
   * </ul>
   */
  private Integer status;
  /**
   * 合并策略
   */
  private boolean replaceExists;

}
