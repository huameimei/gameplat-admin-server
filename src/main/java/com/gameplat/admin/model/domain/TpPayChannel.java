package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/** @Description 在线支付通道实体层 */
@Data
@TableName("tp_pay_channel")
public class TpPayChannel {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "名称")
  private String name;

  @ApiModelProperty(value = "商户ID")
  private Long merchantId;

  @ApiModelProperty(value = "第三方通道编码")
  private String tpPayType;

  @ApiModelProperty(value = "用户层级: 以半角逗号 , 分隔")
  private String memberLevels;

  @ApiModelProperty(value = "备注信息")
  private String remarks;

  @ApiModelProperty(value = "通道备注（仅后台可见）")
  private String chanDesc;

  @ApiModelProperty(value = "排序值")
  private Integer sort;

  @ApiModelProperty(value = "状态: [1 - 启用, 0 - 禁用]")
  private Integer status;

  @ApiModelProperty(value = "充值次数")
  private Long rechargeTimes;

  @ApiModelProperty(value = "充值金额")
  private BigDecimal rechargeAmount;

  @ApiModelProperty(value = "限制信息")
  private String limitInfo;

  @ApiModelProperty(value = "通道提示")
  private String payChannelTip;

  private Integer typeSubscript;

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
