package com.gameplat.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysRoleMenu implements Serializable {

  private Integer roleId;

  private Integer menuId;

  private Integer halfId;

}
