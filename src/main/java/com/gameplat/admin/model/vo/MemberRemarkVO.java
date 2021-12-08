package com.gameplat.admin.model.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 会员银行 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRemarkVO implements Serializable {

  public Long id;

  private Long memberId;

  private String account;

  private String content;

  private String createBy;

  private Date createTime;

  private String updateBy;

  private Date updateTime;
}
