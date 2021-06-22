package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/** @author Lenovo 会员充值层级 */
@Data
public class SysLayerConfig extends BaseEntity {

  /** 层级名称 */
  private String layerName;

  /** 层级值 */
  private Integer layerValue;

  /** 层级类型 */
  private Integer layerType;

  /** 充值总次数 */
  private Integer rechargeTotalTime;

  /** 充值总金额 */
  private Integer rechargeAmountTotal;

  /** 锁定会员数量 */
  private Integer lockMemberNum;

  /** 会员出款次数 */
  private Integer memberOutTime;

  /** 是否限制提现 0不限制 1限制 */
  private Integer withdrawLimit;

  /** 备注 */
  private String remark;

  /** 状态 0禁用 1启用 */
  private Integer status;

  /** 会员人数 */
  private Integer layerPersonNum;

  /** 是否锁定（0 锁定 1带锁定） */
  private Integer lockState;
}
