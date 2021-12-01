package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 提现限制说明 每笔充值都要对应个打码量,只有会员有效打码量达到才可以提现 条件：首先必须先达到最近一笔的打码量,在次总打码量必须达到,两个条件必须满足
 */
@Data
@TableName("valid_withdraw")
public class ValidWithdraw implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 会员ID
   */
  private Long memberId;
  /**
   * 会员账号
   */
  private String account;
  /**
   * 对应充值记录ID
   */
  private Long rechId;
  private BigDecimal rechMoney;
  private BigDecimal discountMoney;
  /**
   * 常态打码量
   */
  private BigDecimal mormDml;
  /**
   * 优惠打码量
   */
  private BigDecimal discountDml;
  private BigDecimal cpDml;
  private BigDecimal sportsDml;
  private BigDecimal videoDml;
  private Integer status;
  /**
   * 0表示最新一笔充值1表示其它
   */
  private Integer type;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  @TableField(fill = FieldFill.INSERT)
  private Date updateTime;

  private String remark;

  @Override
  public String toString() {
    return "ValidWithdraw{" +
        "id=" + id +
        ", memberId=" + memberId +
        ", account='" + account + '\'' +
        ", rechId=" + rechId +
        ", rechMoney=" + rechMoney +
        ", discountMoney=" + discountMoney +
        ", mormDml=" + mormDml +
        ", discountDml=" + discountDml +
        ", cpDml=" + cpDml +
        ", sportsDml=" + sportsDml +
        ", videoDml=" + videoDml +
        ", status=" + status +
        ", type=" + type +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", remark='" + remark + '\'' +
        '}';
  }
}
