package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 会员月报表 @Author : cc @Date : 2022/3/23 */
@Data
public class MemberReportVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "统计月份")
  private String countMonth;

  @Schema(description = "父级ID")
  private Long parentId;

  @Schema(description = "父级账号")
  private String parentName;

  @Schema(description = "账号ID")
  private Long userId;

  @Schema(description = "账号")
  private String userName;

  @Schema(description = "代理结构")
  private String agentPath;

  @Schema(description = "充值金额")
  private BigDecimal rechargeAmount;

  @Schema(description = "有效投注金额")
  private BigDecimal validAmount;

  @Schema(description = "返水")
  private BigDecimal rebateAmount;

  @Schema(description = "红利")
  private BigDecimal dividendAmount;

  @Schema(description = "是否有效：1有效会员 0无效会员")
  private Integer efficient;
}
