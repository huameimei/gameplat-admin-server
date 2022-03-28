package com.gameplat.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** 会员转代理备份 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberTransBackupDTO implements Serializable {

  private Long userId;

  private String account;

  private Integer agentLevel;

  private Long parentId;

  private String parentName;

  private String superPath;

  private String target;
}
