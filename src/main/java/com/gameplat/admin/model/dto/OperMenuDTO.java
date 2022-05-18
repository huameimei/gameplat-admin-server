package com.gameplat.admin.model.dto;

import com.gameplat.common.group.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 操作菜单信息DTO
 *
 * @author three
 */
@Data
public class OperMenuDTO {

  @NotNull(groups = Groups.UPDATE.class, message = "菜单编号不能为空")
  private Long menuId;

  /** 菜单名称 */
  @Schema(description = "菜单名称")
  private String menuName;

  /** 菜单标题 */
  @Schema(description = "菜单标题")
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "菜单标题不能为空")
  private String title;

  /** 父菜单ID */
  @Schema(description = "父菜单ID")
  private Long parentId;

  /** 父菜单名称 */
  @Schema(description = "父菜单名称")
  private String parentName;

  /** 显示顺序 */
  @Schema(description = "排序值")
  @NotNull(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "排序值不能为空")
  private Integer menuSort;

  /** Web前端组件 */
  @Schema(description = "前端组件")
  private String component;

  /** 路径 */
  @Schema(description = "页面路径")
  private String path;

  /** 菜单URL */
  @Schema(description = "资源链接url")
  private String url;

  /** 类型:0目录,1菜单,2按钮 */
  @Schema(description = "类型: M目录,C菜单,B按钮")
  @NotEmpty(
      groups = {Groups.INSERT.class, Groups.UPDATE.class},
      message = "类型不能为空")
  private String menuType;

  /** 菜单状态 0显示1隐藏 */
  @Schema(description = "菜单状态")
  private Integer visible;

  /** 是否外链菜单 (0、否1、是) */
  @Schema(description = "是否外链菜单")
  private Integer iFrame;

  /** 是否缓存 0不缓存、1缓存 */
  @Schema(description = "是否缓存")
  private Integer cacheFlag;

  /** 权限字符串 */
  @Schema(description = "权限字符串")
  private String perms;

  /** 菜单图标 */
  @Schema(description = "图标")
  private String icon;

  /** 菜单状态:1启用 0禁用 */
  @Schema(description = "菜单状态: 1启用 0禁用")
  private Integer status;
}
