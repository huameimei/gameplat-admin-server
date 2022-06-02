package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.base.common.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MemberWithdrawReportVo implements Serializable {

  private Long id;

  @Schema(description = "提现流水号")
  @Excel(name = "提现流水号", width = 40, isImportField = "true_st")
  private String cashOrderNo;

  @Schema(description = "会员ID")
  private Long memberId;

  @Schema(description = "申请人登录账号")
  @Excel(name = "会员账号", width = 20, isImportField = "true_st")
  private String account;

  @Schema(description = "会员真实姓名")
  @Excel(name = "会员真实姓名", width = 20, isImportField = "true_st")
  private String realName;

  @Schema(description = "用户提现时账户余额(提现前的余额)")
  @Excel(name = "提现前的余额", width = 20, isImportField = "true_st")
  private BigDecimal accountMoney;

  @Schema(description = "申请提现金额，单位为分(包含手续费)")
  @Excel(name = "申请提现金额", width = 20, isImportField = "true_st")
  private BigDecimal cashMoney;

  @Schema(description = "申请人银行名称")
  @Excel(name = "申请人银行名称", width = 20, isImportField = "true_st")
  private String bankName;

  @Schema(description = "申请人银行卡号")
  @Excel(name = "申请人银行卡号", width = 40, isImportField = "true_st")
  private String bankCard;

  @Schema(description = "申请人银行地址")
  @Excel(name = "申请人银行地址", width = 20, isImportField = "true_st")
  private String bankAddress;

  @Schema(description = "取现方式1会员取款2后台取款")
  @Excel(name = "取现方式", replace = {"会员取款_1", "后台取款_2"}, width = 20, isImportField = "true_st")
  private Integer cashMode;

  @Schema(description = "申请缘由")
  @Excel(name = "申请缘由", width = 40, isImportField = "true_st")
  private String cashReason;

  @Schema(description = "申请状态：1-未受理 2-受理中；3-已出款；4-已取消；5-拒绝出款 6-撤销出款订单")
  @Excel(name = "申请状态", replace = {"未受理_1", "受理中_2", "已入款_3", "已取消_4", "拒绝出款_5", "撤销出款订单_6"}, width = 12, isImportField = "true_st")
  private Integer cashStatus;

  @Schema(description = "受理人")
  @Excel(name = "受理人", width = 20, isImportField = "true_st")
  private String acceptAccount;

  @Schema(description = "受理时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date acceptTime;


  @Excel(name = "受理时间", width = 40, isImportField = "true_st")
  private String acceptTimeStr;

  private String getAcceptTimeStr() {
    return DateUtil.dateToStr(this.acceptTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  @TableField(fill = FieldFill.UPDATE)
  @Schema(description = "操作人(总管理后台审核的人)")
  @Excel(name = "审核人", width = 20, isImportField = "true_st")
  private String operatorAccount;

  @TableField(fill = FieldFill.UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "操作时间(总管理后台审核的时间)")
  private Date operatorTime;


  @Excel(name = "审核时间", width = 40, isImportField = "true_st")
  private String operatorTimeStr;

  private String getOperatorTimeStr() {
    return DateUtil.dateToStr(this.operatorTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  @Schema(description = "风控审核人员")
  private String riskOpraccount;

  @Schema(description = "风控审核时间")
  private Date riskOprtime;

  @Schema(description = "风控状态：1-未受理 2-受理中 3-通过 4未通过")
  private Integer riskStatus;

  @Schema(description = "审批原因")
  @Excel(name = "审批原因", width = 40, isImportField = "true_st")
  private String approveReason;

  @Schema(description = "审批提现金额，单位为元(扣手续费)")
  @Excel(name = "审批提现金额", width = 20, isImportField = "true_st")
  private BigDecimal approveMoney;

  @Schema(description = "手续费")
  @Excel(name = "手续费", width = 20, isImportField = "true_st")
  private BigDecimal counterFee;

  @Schema(description = "会员层级")
  @Excel(name = "会员层级", width = 20, isImportField = "true_st")
  private Integer memberLevel;

  @Schema(description = "代理会员ID")
  private Long superId;

  @Schema(description = "代理会员账号")
  @Excel(name = "代理会员账号", width = 20, isImportField = "true_st")
  private String superName;

  @Schema(description = "所属上级路径")
  private String superPath;

  @Schema(description = "校验字段")
  private String fk;

  @Schema(description = "报警提示0不报警1出现异常，报警")
  private Integer policeFlag;

  @Schema(description = "浏览器")
  private String browser;

  @Schema(description = "操作系统")
  private String macOs;

  @Schema(description = "IP地址")
  private String ipAddress;

  @Schema(description = "完整信息")
  private String userAgent;

  @Schema(description = "代付状态：处理中<0>，成功<1>，失败<2>")
  private Integer proxyPayStatus;

  @Schema(description = "代付结果描述")
  private String proxyPayDesc;

  @Schema(description = "计算积分: [0 - 否, 1 - 是]")
  private Integer pointFlag;

  @Schema(description = "第三方接口")
  private String ppInterface;

  @Schema(description = "第三方接口名称")
  @Excel(name = "第三方接口名称", width = 20, isImportField = "true_st")
  private String ppInterfaceName;

  @Schema(description = "第三方商户ID")
  private Long ppMerchantId;

  @Schema(description = "第三方商户名称")
  @Excel(name = "第三方商户名称", width = 20, isImportField = "true_st")
  private String ppMerchantName;

  @Schema(description = "会员出款备注")
  @Excel(name = "会员出款备注", width = 40, isImportField = "true_st")
  private String memberMemo;

  @Schema(description = "用户类型（会员:M 代理：A 推广:P 试玩 :T）")
  @Excel(name = "会员出款备注", replace = {"会员_M", "代理_A", "推广_P", "试玩_T"}, width = 40, isImportField = "true_st")
  private String memberType;

  @Schema(description = "取款类型（BANK 银行   USDT BTC ）")
  @Excel(name = "会员出款备注", replace = {"银行_BANK"}, width = 40, isImportField = "true_st")
  private String withdrawType;

  @Schema(description = "虚拟货币汇率")
  @Excel(name = "虚拟货币汇率", width = 20, isImportField = "true_st")
  private BigDecimal currencyRate;

  @Schema(description = "币种数量")
  @Excel(name = "币种数量", width = 20, isImportField = "true_st")
  private String currencyCount;

  @Schema(description = "虚拟货币实际提现汇率")
  private BigDecimal approveCurrencyRate;

  @Schema(description = "虚拟货币实际提现数量")
  private String approveCurrencyCount;

  @TableField(fill = FieldFill.INSERT)
  @Schema(description = "申请时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @Excel(name = "申请时间", width = 40, isImportField = "true_st")
  private String createTimeStr;

  private String getCreateTimeStr() {
    return DateUtil.dateToStr(this.operatorTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}
