package com.gameplat.admin.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 免提直充配置实体类
 */
@Data
public class DirectChargeVo {

  /**
   * 打码量预设常态
   */
  private int normalDmlMultiple;

  /**
   * 打码量预设优惠
   */
  private int discountDmlMultiple;

  /**
   * 计算积分
   */
  private int pointFlag;

  /**
   * 稽查打码量
   */
  private int dmlFlag;

  /**
   * 充值优惠类型
   */
  private String discountType;

  /**
   * 优惠百分比
   */
  private BigDecimal discountPercentage;

  /**
   * 审核备注
   */
  private String auditRemarks;

  /**
   * 会员备注
   */
  private String remarks;

  /**
   * 直接入款
   */
  private int skipAuditing;

  /**
   * 层级
   */
  private String levels;

  @Override
  public String toString() {
    return "DirectCharge{"
            + "normalDmlMultiple="
            + normalDmlMultiple
            + ", discountDmlMultiple="
            + discountDmlMultiple
            + ", pointFlag="
            + pointFlag
            + ", dmlFlag="
            + dmlFlag
            + ", discountType='"
            + discountType
            + '\''
            + ", discountPercentage="
            + discountPercentage
            + ", auditRemarks='"
            + auditRemarks
            + '\''
            + ", remarks='"
            + remarks
            + '\''
            + ", skipAuditing="
            + skipAuditing
            + ", levels='"
            + levels
            + '\''
            + '}';
  }
}
