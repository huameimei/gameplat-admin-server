package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayAccountVO implements Serializable {

  private Long id;

  private String name;

  private String account;

  private String owner;

  private String payType;

  private String bankName;

  private String bankAddress;

  private String qrCode;

  private String memberLevels;

  private String remarks;

  private Integer sort;

  private String url;

  @Schema(description = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  private Long rechargeTimes;

  private BigDecimal rechargeAmount;

  private String orderRemark;

  @Schema(description = "0:启用，1：关闭")
  private Integer orderRemarkStatus;

  private String type;

  private String limitInfo;

  private String handleTip;

  private String emailAddress;

  private Integer typeSubscript;

  /** 通道金额设置标识，0位禁用 */
  private Integer limitStatus;

  /** 通道金额收款上限 */
  private BigDecimal limitAmount;

  /** 通道时间设置标识，0为启用时间设置，1位禁用时间设置 */
  private Integer channelTimeStatus;

  /** 通道显示开始时间 */
  private Integer channelTimeStart;

  /** 通道显示结束时间 */
  private Integer channelTimeEnd;

  /** 通道展示端，1展示在电脑，2展示在安卓，3展示在IOS */
  private String channelShows;

  /** 通道单笔金额金额最小值 */
  private BigDecimal minAmountPerOrder;

  /** 通道单笔金额金额最小值 */
  private BigDecimal maxAmountPerOrder;

  /** 通道风控金额类型 0.任何金额 1.浮动金额 2.固定金额 3浮动固定金额 */
  private Integer riskControlType;

  /** 风控值 */
  private String riskControlValue;

  /** 虚拟货币类型 */
  private String currencyType;

  /** 创建者 */
  @Schema(description = "创建者")
  private String createBy;

  /** 创建时间 */
  @Schema(description = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  /** 更新者 */
  @Schema(description = "更新者")
  private String updateBy;

  /** 更新时间 */
  @Schema(description = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  /** 存款银行卡显示开关  1-是 0-否*/
  private Integer bankAccountSwitch;
}
