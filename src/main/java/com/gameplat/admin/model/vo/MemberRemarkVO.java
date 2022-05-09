package com.gameplat.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/** 会员银行 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRemarkVO implements Serializable {

  private Long id;

  private Long memberId;

  private String account;

  private String content;

  private String createBy;

  private Date createTime;

  private String updateBy;

  private Date updateTime;
}
