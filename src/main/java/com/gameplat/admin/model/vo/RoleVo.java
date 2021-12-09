package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;
import org.dozer.Mapping;

/**
 * 角色Vo
 *
 * @author three
 */
@Data
public class RoleVo {

  @Mapping(value = "roleId")
  private Long id;
  /** 角色名称 */
  private String roleName;

  /** 角色权限 */
  private String roleKey;

  /** 角色排序 */
  private Integer roleSort;

  /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限） */
  private String dataScope;

  /** 是否系统默认 */
  private Integer defaultFlag;

  /** 角色状态（0正常 1停用） */
  private Integer status;

  private Long[] menuIds;
  private Date createTime;
  private Date updateTime;
  private String remark;
}
