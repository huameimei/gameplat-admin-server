package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 打码量出参 @Author : lily @Date : 2021/11/27
 */
@Data
public class ValidWithdrawVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "用户id")
  private Long memberId;

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "充值记录ID(来源id)")
  private String rechId;

  @Schema(description = "充值金额")
  private BigDecimal rechMoney;

  @Schema(description = "优惠金额")
  private BigDecimal discountMoney;

  @Schema(description = "类型： 0表示最新一笔充值1表示其它")
  private Integer type;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "修改时间")
  private Date updateTime;

  @Schema(description = "常态打码量")
  private BigDecimal mormDml;

  @Schema(description = "优惠打码量")
  private BigDecimal discountDml;

  @Schema(description = "彩票打码量")
  private BigDecimal cpDml;

  @Schema(description = "体育打码量")
  private BigDecimal sportsDml;

  @Schema(description = "真人打码量")
  private BigDecimal videoDml;

  @Schema(description = "状态: 0正常,1表示已出款")
  private Integer status;

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "截止时间")
  private Date endTime;

  @Schema(description = "投注内容")
  private String betContext;
}
