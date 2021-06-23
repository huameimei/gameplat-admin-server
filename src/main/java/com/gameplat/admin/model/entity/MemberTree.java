package com.gameplat.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class MemberTree implements Serializable {

  /** 父级 */
  private Long ancestor;

  /** 子级 */
  private Long descendant;

  /** 距离 */
  private Long distance;
}
