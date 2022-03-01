package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/2/8 18:34 @Version 1.0
 */
@Data
public class MemberReportDto implements Serializable {

  /** 开始日期 */
  private String startTime;

  /** 结束日期 */
  private String endTime;

  /** 是否含线下(1 含线下) */
  private Integer type;

  /** 账户 */
  private String username;
}
