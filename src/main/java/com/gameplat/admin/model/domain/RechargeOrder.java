package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recharge_order")
public class RechargeOrder {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "校验字段")
  private String fk;

  @ApiModelProperty(value = "会员ID")
  private Long memberId;

  @ApiModelProperty(value = "会员登录账户")
  private String account;

  @ApiModelProperty(value = "会员真实姓名")
  private String nickname;

  @ApiModelProperty(value = "会员层级")
  private String memberLevel;

  @ApiModelProperty(value = "会员充值前的余额")
  private BigDecimal balance;

  @ApiModelProperty(value = "所属代理ID")
  private Long superId;

  @ApiModelProperty(value = "所属代理账号")
  private String superAccount;

  @ApiModelProperty(value = "所属上级路径")
  private String superPath;

  @ApiModelProperty(value = "充值方式: [1 - 转账汇款, 2 - 在线支付, 3 - 人工充值]")
  private Integer mode;

  @ApiModelProperty(value = "充值订单号(充值流水号)")
  private String orderNo;

  @ApiModelProperty(value = "支付类型")
  private String payType;

  @ApiModelProperty(value = "支付类型名称")
  private String payTypeName;

  @ApiModelProperty(value = "收款账户ID")
  private Long payAccountId;

  @ApiModelProperty(value = "收款账号")
  private String payAccountAccount;

  @ApiModelProperty(value = "收款人")
  private String payAccountOwner;

  @ApiModelProperty(value = "收款银行名称")
  private String payAccountBankName;

  @ApiModelProperty(value = "第三方接口编码")
  private String tpInterfaceCode;

  @ApiModelProperty(value = "第三方接口名称")
  private String tpInterfaceName;

  @ApiModelProperty(value = "第三方商户ID")
  private Long tpMerchantId;

  @ApiModelProperty(value = "第三方商户名称")
  private String tpMerchantName;

  @ApiModelProperty(value = "第三方通道ID")
  private Long tpPayChannelId;

  @ApiModelProperty(value = "第三方通道名称")
  private String tpPayChannelName;

  @ApiModelProperty(value = "第三方订单号")
  private String tpOrderNo;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal amount;

  @ApiModelProperty(value = "优惠金额")
  private BigDecimal discountAmount;

  @ApiModelProperty(value = "是否充值优惠: [0 - 否, 1 - 是]")
  private Integer discountRechargeFlag;

  @ApiModelProperty(value = "优惠类型")
  private Integer discountType;

  @ApiModelProperty(value = "充值总金额: 充值金额 + 优惠金额")
  private BigDecimal totalAmount;

  @ApiModelProperty(value = "计算积分标识:  [0 - 否, 1 - 是]")
  private Integer pointFlag;

  @ApiModelProperty(value = "计算打码量标识: [0 - 否, 1 - 是]")
  private Integer dmlFlag;

  @ApiModelProperty(value = "常态打码量")
  private BigDecimal normalDml;

  @ApiModelProperty(value = "优惠打码量")
  private BigDecimal discountDml;

  @ApiModelProperty(value = "存款人")
  private String rechargePerson;

  @ApiModelProperty(value = "存款时间")
  private Date rechargeTime;

  @ApiModelProperty(value = "充值状态：1-未受理 2-受理中；3-已入款；4-已取消")
  private Integer status;

  @ApiModelProperty(value = "备注信息")
  private String remarks;

  /**
   * 审核人
   */
  @ApiModelProperty(value = "审核人")
  private String auditorAccount;

  @ApiModelProperty(value = "审核时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date auditTime;

  @ApiModelProperty(value = "审核备注")
  private String auditRemarks;

  @ApiModelProperty(value = "浏览器")
  private String browser;

  @ApiModelProperty(value = "操作系统")
  private String os;

  @ApiModelProperty(value = "IP地址")
  private String ipAddress;

  @ApiModelProperty(value = "完整信息")
  private String userAgent;

  @ApiModelProperty(value = "请求域名")
  private String domain;

  @ApiModelProperty(value = "非银行支付的支付方式(1:扫码支付 2：好友支付 3：公众号支付)")
  private Integer subPayType;

  @ApiModelProperty(value = "会员类型HY会员TEST试玩会员,VHY 推广账号")
  private String memberType;

  @ApiModelProperty(value = "币种汇率")
  private BigDecimal currencyRate;

  @ApiModelProperty(value = "币种数量")
  private String currencyCount;

  @ApiModelProperty(value = "区块链交易ID")
  private String currencyTranceId;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

}
