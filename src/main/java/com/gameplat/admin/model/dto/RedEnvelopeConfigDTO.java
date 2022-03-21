package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.common.group.Groups;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

/** 红包配置 */
@Data
public class RedEnvelopeConfigDTO {

  /** 主键id */
  private Long id;

  /** 红包名称 */
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "红包名称不能为空")
  private String redName;

  /** 红包金额 */
  private BigDecimal amount;

  /** 打码倍数要求 */
  private Double multiple;

  /** 是否有时效 */
  private Integer isAging;

  /** 领取开始时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date receiveStartTime;

  /** 领取结束时间 */
  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
  private Date receiveEndTime;

  /** 充值要求 */
  private BigDecimal rechargeAmount;

  /** 打码要求 */
  private BigDecimal chipRequire;

  /** 红包状态 0 禁用 1 启用 2 过期 */
  private Integer state;

  /** 领取方式 1 手动领取 2 自动到账 */
  private Integer receiveMethod;

  /** 红包图片地址 */
  private String imgUrl;

  /** 红包展示位置 */
  private String location;

  /** 备注信息 */
  private String remark;

  /** 创建人 */
  private String createBy;

  /** 更新人 */
  private String updateBy;
}
