package com.gameplat.admin.model.vo;

import com.gameplat.common.model.entity.BaseEntity;
import java.util.List;
import lombok.Data;

/**
 * 菜单实体类
 *
 * @author Lenovo
 */
@Data
public class SysMenuVo extends BaseEntity {

  /** 父类ID */
  private Long parentId;

  /** 组件名称 */
  private String name;

  /** url */
  private String url;

  /** 图标 */
  private String icon;

  /** 排序 */
  private Integer sort;

  /** 菜单类型 1--目录 2--菜单 3--按钮 */
  private Integer type;

  /** 是否是外链菜单 0--否 1--是 */
  private Integer iFrame;

  /** 菜单缓存 0--否 1--是 */
  private Integer cache;

  /** 是否隐藏,0--否 1--是 */
  private Integer hidden;

  /** 菜单标题 */
  private String title;

  /** 菜单标签 */
  private String label;

  /** 链接地址 */
  private String menuPath;

  /** 组件名称 */
  private String component;

  /** 路径 */
  private String path;

  private String redirect;

  /** 权限标识 */
  private String authMark;

  private Long halfId;

  private MetaVo meta;

  /**
   * 子菜单集合
   */
  private List<SysMenuVo> subMenus;
}
