package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("member_withdraw_history")
public class MemberWithdrawHistory {

  public Long id;

  @ApiModelProperty(value = "提现流水号")
  private String cashOrderNo;

  @ApiModelProperty(value = "会员ID")
  private Long memberId;

  @ApiModelProperty(value = "申请人登录账号")
  private String account;

  @ApiModelProperty(value = "会员真实姓名")
  private String nickname;

  @ApiModelProperty(value = "用户提现时账户余额(提现前的余额)")
  private BigDecimal accountMoney;

  @ApiModelProperty(value = "申请提现金额，单位为分(包含手续费)")
  private BigDecimal cashMoney;

  @ApiModelProperty(value = "申请人银行名称")
  private String bankName;

  @ApiModelProperty(value = "申请人银行卡号")
  private String bankCard;

  @ApiModelProperty(value = "申请人银行地址")
  private String bankAddress;

  @ApiModelProperty(value = "取现方式1会员取款2后台取款")
  private Integer cashMode;

  @ApiModelProperty(value = "申请缘由")
  private String cashReason;

  @ApiModelProperty(value = "申请状态：1-未受理 2-受理中；3-已出款；4-已取消；5-拒绝出款 6-撤销出款订单")
  private Integer cashStatus;

  @TableField(fill = FieldFill.UPDATE)
  @ApiModelProperty(value = "操作人(总管理后台审核的人)")
  private String operatorAccount;

  @TableField(fill = FieldFill.UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty(value = "操作时间(总管理后台审核的时间)")
  private Date operatorTime;

  @ApiModelProperty(value = "风控审核人员")
  private String riskOpraccount;

  @ApiModelProperty(value = "风控审核时间")
  private Date riskOprtime;

  @ApiModelProperty(value = "风控状态：1-未受理 2-受理中 3-通过 4未通过")
  private Integer riskStatus;

  @ApiModelProperty(value = "审批原因")
  private String approveReason;

  @ApiModelProperty(value = "审批提现金额，单位为元(扣手续费)")
  private BigDecimal approveMoney;

  @ApiModelProperty(value = "手续费")
  private BigDecimal counterFee;

  @ApiModelProperty(value = "会员层级")
  private String memberLevel;

  @ApiModelProperty(value = "代理会员ID")
  private Long superId;

  @ApiModelProperty(value = "代理会员账号")
  private String superName;

  @ApiModelProperty(value = "所属上级路径")
  private String superPath;

  @ApiModelProperty(value = "校验字段")
  private String fk;

  @ApiModelProperty(value = "报警提示0不报警1出现异常，报警")
  private Integer policeFlag;

  @ApiModelProperty(value = "浏览器")
  private String browser;

  @ApiModelProperty(value = "操作系统")
  private String macOs;

  @ApiModelProperty(value = "IP地址")
  private String ipAddress;

  @ApiModelProperty(value = "完整信息")
  private String userAgent;

  @ApiModelProperty(value = "代付状态：处理中<0>，成功<1>，失败<2>")
  private Integer proxyPayStatus;

  @ApiModelProperty(value = "代付结果描述")
  private String proxyPayDesc;

  @ApiModelProperty(value = "计算积分: [0 - 否, 1 - 是]")
  private Integer pointFlag;

  @ApiModelProperty(value = "第三方接口")
  private String ppInterface;

  @ApiModelProperty(value = "第三方接口名称")
  private String ppInterfaceName;

  @ApiModelProperty(value = "第三方商户ID")
  private Long ppMerchantId;

  @ApiModelProperty(value = "第三方商户名称")
  private String ppMerchantName;

  @ApiModelProperty(value = "会员出款备注")
  private String memberMemo;

  @ApiModelProperty(value = "会员类型HY会员TEST试玩会员,VHY 推广账号")
  private String memberType;

  @ApiModelProperty(value = "取款类型（BANK 银行   USDT BTC ）")
  private String withdrawType;

  @ApiModelProperty(value = "虚拟货币汇率")
  private BigDecimal currencyRate;

  @ApiModelProperty(value = "币种数量")
  private String currencyCount;

  @ApiModelProperty(value = "区块链交易ID")
  private String approveCurrencyCount;

  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "申请时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

}
