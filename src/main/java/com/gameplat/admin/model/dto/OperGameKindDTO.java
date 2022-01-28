package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class OperGameKindDTO implements Serializable {

  private Long id;

  /** 是否开放试玩(0：不支持；1:关闭 2:开启) */
  private Integer demoEnable;

  /**
   * 是否开放(0：否；1:是)
   */
  private Integer enable;

  /**
   * 是否热门(0：否；1:是)
   */
  private Integer hot;

  /**
   * 是否有下级游戏(0：否；1:是)
   */
  private Integer subLevel;

  /**
   * 排序
   */
  private Integer sort;

  /**
   * 返水最高比例
   */
  private Integer rebateRate;

  /**
   * 维护开始时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date maintenanceTimeStart;

  /**
   * 维护结束时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date maintenanceTimeEnd;


  /**
   * 试玩维护开始时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date demoMaintenanceTimeStart;


  /**
   * 试玩维护结束时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date demoMaintenanceTimeEnd;
}
