package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 会员等级福利出参
 * @date 2021/11/22
 */
@Data
public class MemberWealVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "福利名称")
  private String name;

  @Schema(description = "类型 0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包")
  private Integer type;

  @Schema(description = "状态 0:未结算  1：未派发   2：已派发  3：已回收")
  private Integer status;

  @Schema(description = "周期  开始时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date startDate;

  @Schema(description = "周期  结束时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date endDate;

  @Schema(description = "结算时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date settleTime;

  @Schema(description = "派发时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date payTime;

  @Schema(description = "最低充值金额")
  private BigDecimal minRechargeAmount;

  @Schema(description = "最低有效投注金额")
  private BigDecimal minBetAmount;

  @Schema(description = "资格会员数")
  private Integer totalUserCount;

  @Schema(description = "总的派发金额")
  private BigDecimal totalPayMoney;

  @Schema(description = "福利备注")
  private String remark;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "修改人")
  private String updateBy;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "福利描述")
  private String depict;
}
