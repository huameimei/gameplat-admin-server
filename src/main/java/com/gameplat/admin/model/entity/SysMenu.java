package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * 菜单实体类
 *
 * @author Lenovo
 */
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

  /** 父类ID */
  private String parentId;

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

}
