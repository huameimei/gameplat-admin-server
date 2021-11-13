package com.gameplat.admin.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色授权菜单DTO
 *
 * @author three
 */
@Data
public class AuthMenuDTO {

  /** 角色 */
  @NotNull(message = "角色ID不能为空")
  private Long roleId;

  /** 菜单列表（逗号分割） */
  @NotEmpty(message = "菜单ID不能为空")
  private List<Long> menuIds;
}
