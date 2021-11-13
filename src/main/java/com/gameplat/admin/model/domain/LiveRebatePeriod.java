package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("live_rebate_periods")
public class LiveRebatePeriod implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;

  private Date beginDate;

  private Date endDate;

  private String blackAccounts;

  private String blackLevels;

  private Integer status;

  private Date addTime;

  private Date statTime;

  private List<Member> blackAccountList;

  //返水人数
  private int liveRebateCount;

  /**
   * 实际返水金额
   */
  private BigDecimal realRebateMoney;
}
