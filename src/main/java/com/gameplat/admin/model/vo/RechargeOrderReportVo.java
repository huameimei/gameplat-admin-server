package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("recharge_order_history")
public class RechargeOrderReportVo implements Serializable {


  @Schema(description = "会员登录账户")
  @Excel(name = "会员账号", width = 20, isImportField = "true_st")
  private String account;

  @Excel(name = "真实姓名", width = 20, isImportField = "true_st")
  @Schema(description = "会员真实姓名")
  private String nickname;


  @Excel(name = "会员层级", width = 10, isImportField = "true_st")
  @Schema(description = "会员层级")
  private String memberLevel;

  @Excel(name = "充值前的余额", width = 10, isImportField = "true_st")
  @Schema(description = "会员充值前的余额")
  private BigDecimal balance;

  @Schema(description = "所属代理ID")
  private Long superId;

  @Excel(name = "上级代理账号", width = 20, isImportField = "true_st")
  @Schema(description = "所属代理账号")
  private String superAccount;

  @Schema(description = "所属上级路径")
  private String superPath;

  @Excel(name = "充值方式", replace = "转账汇款_1,在线支付_2,人工充值_3", width = 20, isImportField = "true_st")
  @Schema(description = "充值方式: [1 - 转账汇款, 2 - 在线支付, 3 - 人工充值]")
  private Integer mode;

  @Schema(description = "充值订单号(充值流水号)")
  @Excel(name = "充值订单号", width = 30, isImportField = "true_st")
  private String orderNo;

  @Schema(description = "支付类型")
  private String payType;

  @Schema(description = "支付类型名称")
  @Excel(name = "支付类型名称", width = 20, isImportField = "true_st")
  private String payTypeName;

  @Schema(description = "收款账户ID")
  private Long payAccountId;

  @Schema(description = "收款账号")
  @Excel(name = "收款账号", width = 20, isImportField = "true_st")
  private String payAccountAccount;

  @Schema(description = "收款人")
  @Excel(name = "收款人", width = 20, isImportField = "true_st")
  private String payAccountOwner;

  @Schema(description = "收款银行名称")
  @Excel(name = "收款银行", width = 20, isImportField = "true_st")
  private String payAccountBankName;

  @Schema(description = "第三方接口编码")
  private String tpInterfaceCode;

  @Schema(description = "第三方接口名称")
  @Excel(name = "三方支付名称", width = 20, isImportField = "true_st")
  private String tpInterfaceName;

  @Schema(description = "第三方商户ID")
  private Long tpMerchantId;

  @Schema(description = "第三方商户名称")
  @Excel(name = "第三方商户名称", width = 20, isImportField = "true_st")
  private String tpMerchantName;

  @Schema(description = "第三方通道ID")
  private Long tpPayChannelId;

  @Schema(description = "第三方通道名称")
  @Excel(name = "第三方通道名称", width = 20, isImportField = "true_st")
  private String tpPayChannelName;

  @Schema(description = "第三方订单号")
  @Excel(name = "第三方订单", width = 30, isImportField = "true_st")
  private String tpOrderNo;

  @Schema(description = "充值金额")
  @Excel(name = "充值金额", width = 20, isImportField = "true_st")
  private BigDecimal amount;

  @Schema(description = "实际支付金额")
  @Excel(name = "实际支付金额", width = 20, isImportField = "true_st")
  private BigDecimal payAmount;

  @Schema(description = "优惠金额")
  @Excel(name = "优惠金额", width = 20, isImportField = "true_st")
  private BigDecimal discountAmount;

  @Schema(description = "是否充值优惠: [0 - 否, 1 - 是]")
  private Integer discountRechargeFlag;

  @Schema(description = "优惠类型")
  private Integer discountType;

  @Schema(description = "充值总金额: 充值金额 + 优惠金额")
  @Excel(name = "充值总金额", width = 20, isImportField = "true_st")
  private BigDecimal totalAmount;

  @Schema(description = "计算积分标识:  [0 - 否, 1 - 是]")
  private Integer pointFlag;

  @Schema(description = "计算打码量标识: [0 - 否, 1 - 是]")
  private Integer dmlFlag;

  @Schema(description = "常态打码量")
  private BigDecimal normalDml;

  @Schema(description = "优惠打码量")
  private BigDecimal discountDml;

  @Schema(description = "存款人")
  private String rechargePerson;

  @Schema(description = "存款时间")
  private Date rechargeTime;

  @Schema(description = "充值状态：1-未受理 2-受理中；3-已入款；4-已取消")
  @Excel(name = "充值状态", replace = "未受理_1,受理中_2,已入款_3,已取消_4", width = 12, isImportField = "true_st")
  private Integer status;

  @Schema(description = "备注信息")
  @Excel(name = "备注", width = 20, isImportField = "true_st")
  private String remarks;

  @Schema(description = "受理人")
  @Excel(name = "受理人", width = 20, isImportField = "true_st")
  private String acceptAccount;

  @Schema(description = "受理时间")
  @Excel(name = "受理时间", width = 40, isImportField = "true_st")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date acceptTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  @Schema(description = "审核人")
  @Excel(name = "审核人", width = 20, isImportField = "true_st")
  private String auditorAccount;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  @Schema(description = "审核时间")
  @Excel(name = "审核时间", width = 40, isImportField = "true_st")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date auditTime;

  @Schema(description = "审核备注")
  @Excel(name = "审核备注", width = 20, isImportField = "true_st")
  private String auditRemarks;

  @Schema(description = "浏览器")
  private String browser;

  @Schema(description = "操作系统")
  private String os;

  @Schema(description = "IP地址")
  private String ipAddress;

  @Schema(description = "完整信息")
  private String userAgent;

  @Schema(description = "请求域名")
  private String domain;

  @Schema(description = "非银行支付的支付方式(1:扫码支付 2：好友支付 3：公众号支付)")
  private Integer subPayType;

  @Schema(description = "会员类型HY会员TEST试玩会员,VHY 推广账号")
  private String memberType;

  @Schema(description = "币种汇率")
  private Long currencyRate;

  @Schema(description = "币种数量")
  private BigDecimal currencyCount;

  @Schema(description = "区块链交易ID")
  private BigDecimal currencyTranceId;

  @Schema(description = "超时时间")
  private Integer orderExpireTime;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @Schema(description = "创建者")
  private String createBy;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @Schema(description = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @Schema(description = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @Schema(description = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
