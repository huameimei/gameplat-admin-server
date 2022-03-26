package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 查询现金流水出参
 * @date 2021/12/2
 */
@Data
public class MemberBillVO implements Serializable {

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "用户编号")
  private Long memberId;

  @ApiModelProperty(value = "账号")
  @Excel(name = "会员账号", width = 17, isImportField = "true_st")
  private String account;

  @ApiModelProperty(value = "交易类型：TranTypes中值")
  @Excel(
      name = "账变类型",
      width = 16,
      replace = {
        "转账充值_1",
        "线上支付_2",
        "彩票下注_3",
        "彩票派彩_4",
        "用户提款_6",
        "撤单返款_7",
        "提款失败_8",
        "退佣(分红)_10",
        "人工充值_11",
        "人工提现_12",
        "注册送彩金_16",
        "代理返点_17",
        "人工存入_18",
        "给予返水_19",
        "活动优惠_20",
        "追号返款_21",
        "系统奖励_22",
        "代理日工资_23",
        "其他充值_25",
        "打和返款_26",
        "冲销返水_27",
        "人工提出_28",
        "追号扣款_29",
        "优惠扣除_30",
        "其他扣除_31",
        "冲销派奖_32",
        "游戏上分_33",
        "游戏下分_34",
        "体育下注_35",
        "体育结算_36",
        "违规退还本金_37",
        "重新结算_38",
        "取消订单_39",
        "真人返点_40",
        "真人返点回收_41",
        "电竞王者荣耀结算_43",
        "电竞王者荣耀取消订单_44",
        "棋牌支出_50",
        "棋牌收入_51",
        "转出余额宝_52",
        "转入余额宝_53",
        "升级奖励_54",
        "俸禄发放_55",
        "俸禄回收_56",
        "借呗放款_57",
        "借呗还款_58",
        "活动彩金_59",
        "绑定手机送彩_60",
        "充值卡充值_63"
      },
      isImportField = "true_st")
  private Integer tranType;

  @ApiModelProperty(value = "分表ID")
  private Integer tableIndex;

  @ApiModelProperty(value = "订单号，关联其他业务订单号")
  private String orderNo;

  @ApiModelProperty(value = "交易金额")
  @Excel(name = "账变金额", width = 15, isImportField = "true_st")
  private BigDecimal amount;

  @ApiModelProperty(value = "账变前的余额")
  @Excel(name = "账变前的余额", width = 15, isImportField = "true_st")
  private BigDecimal balance;

  @ApiModelProperty(value = "账变内容")
  @Excel(name = "账变内容", width = 20, isImportField = "true_st")
  private String content;

  @ApiModelProperty(value = "操作人")
  @Excel(name = "操作人", width = 15, isImportField = "true_st")
  private String operator;

  @ApiModelProperty(value = "添加时间")
  @Excel(name = "账变时间", width = 20, isImportField = "true_st", exportFormat = "yyyy-MM-dd HH:mm:ss")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
