package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** @Description 在线支付通道实体层 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tp_pay_channel")
public class TpPayChannel extends BaseEntity<TpPayChannel> {

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "商户ID")
  private Integer merchantId;

  @ApiModelProperty(value = "第三方通道编码")
  private String tpPayType;

  @ApiModelProperty(value = "用户层级: 以半角逗号 , 分隔")
  private String userLevels;

  @ApiModelProperty(value = "备注信息")
  private String remarks;

  @ApiModelProperty(value = "通道备注（仅后台可见）")
  private String chanDesc;

  @ApiModelProperty(value = "排序值")
  private Integer sort;

  @ApiModelProperty(value = "状态: [0 - 启用, 1 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "充值次数")
  private Long rechargeTimes;

  @ApiModelProperty(value = "充值金额")
  private Long rechargeAmount;

  @ApiModelProperty(value = "限制信息")
  private String limitInfo;

  @ApiModelProperty(value = "通道提示")
  private String payChannelTip;
}
