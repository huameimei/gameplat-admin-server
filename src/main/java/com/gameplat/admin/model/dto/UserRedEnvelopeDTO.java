package com.gameplat.admin.model.dto;

import lombok.Data;

import java.util.Date;


/**
 * 快捷回复配置DTO
 *
 * @author three
 */
@Data
public class UserRedEnvelopeDTO {

  /**
   * 会员账号
   */
  private String userName;

  /**
   * 红包领取状态 0失效 1未领取 2已领取 3回收
   */
  private Integer status;

  /**
   * 红包来源ID
   */
  private String sourceId;

  /**
   * 领取时间区间查询 开始时间
   */
  private Date startTime;

  /**
   * 领取时间区间查询 结束时间
   */
  private Date endTime;

  /**
   * 红包 id
   */
  private Long redEnvelopeId;
}
