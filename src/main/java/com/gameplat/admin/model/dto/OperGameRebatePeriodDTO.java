package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.model.entity.member.Member;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OperGameRebatePeriodDTO implements Serializable {

  private Long id;

  private String name;

  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private Date beginDate;

  @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
  private Date endDate;

  private String blackAccounts;

  private String blackLevels;

  private Integer status;

  private Date statTime;

  private List<Member> blackAccountList;

  /** 返水人数 */
  private int liveRebateCount;

  /** 实际返水金额 */
  private BigDecimal realRebateMoney;

  private Boolean only;
}
