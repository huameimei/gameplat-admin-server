package com.gameplat.admin.model.vo;

import com.gameplat.admin.model.domain.SysMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuVo {

  /**
   * 菜单ID
   */
  private Long menuId;

  /**
   * 菜单名称
   */
  private String menuName;

  /**
   * 菜单标题
   */
  private String title;

  /**
   * 父菜单ID
   */
  private Long parentId;

  /**
   * 父菜单名称
   */
  private String parentName;

  /**
   * 显示顺序
   */
  private Integer menuSort;

  /**
   * Web前端组件
   */
  private String component;

  /**
   * 路径
   */
  private String path;

  /**
   * 菜单URL
   */
  private String url;

  /**
   * 类型:0目录,1菜单,2按钮
   */
  private String menuType;

  /**
   * 0显示1隐藏
   */
  private Integer visible;

  /**
   * 是否外链菜单
   * (0、否1、是)
   */
  private Integer iFrame;
  /**
   * 是否缓存菜单
   */
  private Integer cacheFlag;

  /**
   * 权限字符串
   */
  private String perms;

  /**
   * 菜单图标
   */
  private String icon;

  /**
   * 菜单状态:1启用 0禁用
   */
  private Integer status;

  /**
   * 子菜单
   */
  private List<SysMenu> children = new ArrayList<SysMenu>();
}
