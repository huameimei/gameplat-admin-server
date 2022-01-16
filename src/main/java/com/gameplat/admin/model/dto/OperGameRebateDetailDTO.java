package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class OperGameRebateDetailDTO implements Serializable {

  private Long id;

  private Long memberId;

  private String account;

  private String realName;

  private String userPaths;

  private Date createTime;

  private BigDecimal rebateMoney;

  private BigDecimal realRebateMoney;

  private Long periodId;

  private String periodName;

  private Date beginDate;

  private Date endDate;

  private Integer status;

  private String remark;

  private BigDecimal validAmount;

  private String statTime;


}
