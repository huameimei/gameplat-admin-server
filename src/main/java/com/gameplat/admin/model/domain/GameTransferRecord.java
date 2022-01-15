package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@TableName("game_transfer_record")
public class GameTransferRecord implements Serializable {
  /**
   * 主键
   */
  @TableId(type = IdType.AUTO)
  private Long id;


  /**
   * 订单编号
   */
  private String orderNo;


  /**
   * 会员ID
   */
  private Long memberId;

  /**
   * 会员账号
   */
  private String account;


  /**
   * 转账金额
   */
  private BigDecimal amount;

  /**
   * 转账类型：1转入，2转出
   */
  private Integer transferType;


  /**
   * 1平台转出成功真人转入失败,
   * 2真人转出成功平台转入失败，
   * 3受理成功,
   * 4取消
   */
  private Integer status;


  /**
   * 真人平台编码
   */
  private String platformCode;


  /**
   * 备注
   */
  private String remark;

  /**
   * 余额
   */
  private BigDecimal balance;

  /**
   * 类型:0:手动转换  1:自动转换
   */
  private Integer transferStatus;


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
  @TableField(fill = FieldFill.UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

}
