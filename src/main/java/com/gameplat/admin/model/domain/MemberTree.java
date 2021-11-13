package com.gameplat.admin.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberTree implements Serializable {

  /** 父级 */
  private Long ancestor;

  /** 子级 */
  private Long descendant;

  /** 距离 */
  private Long distance;
}
