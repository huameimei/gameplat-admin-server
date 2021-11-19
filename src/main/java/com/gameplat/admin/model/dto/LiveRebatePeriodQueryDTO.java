package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class LiveRebatePeriodQueryDTO implements Serializable{

  private Long id;

  private String name;

  /**
   * 开始时间
   */
  private String startTime;

  /**
   * 结束时间
   */
  private String endTime;

  /**
   * 锁定会员
   */
  private String blackAccounts;

  /**
   * 锁定层级
   */
  private String blackLevels;

  /**
   * 状态
   */
  private Integer status;

}
