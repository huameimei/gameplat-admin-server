package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/** @Description : 会员月报表 @Author : cc @Date : 2022/3/23 */
@Data
public class MemberReportVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "统计月份")
  private String countMonth;

  @ApiModelProperty(value = "父级ID")
  private Long parentId;

  @ApiModelProperty(value = "父级账号")
  private String parentName;

  @ApiModelProperty(value = "账号ID")
  private Long userId;

  @ApiModelProperty(value = "账号")
  private String userName;

  @ApiModelProperty(value = "代理结构")
  private String agentPath;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal rechargeAmount;

  @ApiModelProperty(value = "有效投注金额")
  private BigDecimal validAmount;

  @ApiModelProperty(value = "返水")
  private BigDecimal rebateAmount;

  @ApiModelProperty(value = "红利")
  private BigDecimal dividendAmount;

  @ApiModelProperty(value = "是否有效：1有效会员 0无效会员")
  private Integer efficient;
}
