package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 会员银行 */
@Data
@EqualsAndHashCode(of = "id")
@TableName("member_bank")
public class MemberBank {

  @TableId(type = IdType.AUTO)
  public Long id;

  private Long memberId;

  private String bankName;

  private String address;

  private String cardNo;

  private String type;

  private String currency;

  private String isDefault;

  private String remark;

  private String network;

  private String alias;

  private String cardHolder;

  /** 创建者 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 更新者 */
  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;
}
