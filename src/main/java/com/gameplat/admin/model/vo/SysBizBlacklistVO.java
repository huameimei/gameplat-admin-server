package com.gameplat.admin.model.vo;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysBizBlacklistVO extends BaseEntity {

  /** 目标类型 0: 会员 1: 层级 */
  private Integer targetType;

  /** 目标: 会员帐号、层级值 */
  private String target;

  /** 黑名单类型 */
  private String types;

  /** 备注 */
  private String remark;

  /** 状态{0: 禁用, 1: 启用} */
  private Integer status;
}
