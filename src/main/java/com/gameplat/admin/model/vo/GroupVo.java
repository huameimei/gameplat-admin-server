package com.gameplat.admin.model.vo;

import lombok.Data;
import org.dozer.Mapping;

import java.util.Date;

/**
 * 分组VO
 *
 * @author three
 */
@Data
public class GroupVo {

  @Mapping(value = "roleId")
  private Long id;

  @Mapping(value = "roleName")
  private String groupName;

  @Mapping(value = "roleKey")
  private String groupKey;

  private Date createTime;
  private Date updateTime;
  private Long[] menuIds;
  private String remark;
}
