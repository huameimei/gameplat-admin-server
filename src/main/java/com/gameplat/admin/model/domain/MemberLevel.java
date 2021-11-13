package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 会员层级
 *
 * @author robben
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("member_level")
public class MemberLevel {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String levelName;

  private Integer levelValue;

  private Integer totalRechTimes;

  private BigDecimal totalRechAmount;

  private Integer memberNum;

  private Integer memberLockNum;

  private Integer dayOfWithdraw;

  private Integer locked;

  private Integer status;

  private Integer isWithdraw;

  private Integer isDefault;
}
