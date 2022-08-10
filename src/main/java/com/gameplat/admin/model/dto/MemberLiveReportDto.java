package com.gameplat.admin.model.dto;

import lombok.Data;

/** @Author kb @Date 2022/2/8 18:34 @Version 1.0 */
@Data
public class MemberLiveReportDto {
  /** 用户名 */
  private String account;
  /** 上级名称 */
  private String parentName;
  /** 是否直属 */
  private Boolean isDirect = false;
  /** 用户充值层级 */
  private Integer userLevel;
  /** 开始日期 */
  private String startTime;
  /** 结束日期 */
  private String endTime;
  /** 排序字段 */
  private String orderColumn;
  /** 排序方式 */
  private String sortType;

  private Integer pSize;
  private Integer from;
  private String exportKey;
}
