package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperGameKindDTO implements Serializable {

  private Long id;

  /** 是否开放试玩(0：否；1:是) */
  private Integer enableDemo;

  /**
   * 是否开放(0：否；1:是)
   */
  private Integer enabled;

  /**
   * 排序
   */
  private Integer sort;

  /**
   * 维护开始时间
   */
  private Date maintenanceTimeStart;

  /**
   * 维护结束时间
   */
  private Date maintenanceTimeEnd;


  /**
   * 试玩维护开始时间
   */
  private Date demoMaintenanceTimeStart;


  /**
   * 试玩维护结束时间
   */
  private Date demoMaintenanceTimeEnd;

}
