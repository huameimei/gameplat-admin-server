package com.gameplat.admin.model.vo;

import com.gameplat.admin.model.domain.Member;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class GameRebatePeriodVO implements Serializable {

  private Long id;

  private String name;

  private Date beginDate;

  private Date endDate;

  private String blackAccounts;

  private String blackLevels;

  private Integer status;

  private Date createTime;

  private Date statTime;

  private List<Member> blackAccountList;

  //返水人数
  private int liveRebateCount;

  /**
   * 实际返水金额
   */
  private BigDecimal realRebateMoney;

}
