package com.gameplat.admin.model.bean.router;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 构建 Vue路由
 *
 * @author three
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VueRouter<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 菜单ID */
  private Long menuId;

  /**
   * 父id
   */
  private Long parentId;

  /** 父标题 */
  private String parentName;
  /** 页面路径/资源链接url */
  private String path;

  /** 菜单名称 */
  private String name;

  /** 菜单标题 */
  private String title;

  /** Web前端组件 */
  private String component;

  /** 图标 */
  private String icon;

  /** 跳转路径 */
  private String redirect;

  /** 菜单类型 */
  private String menuType;

  /** 权限字符串 */
  private String perms;

  /** 显示顺序 */
  private Long orderNum;

  /** 菜单状态:0显示,1隐藏 */
  private Long status;

  /** 授权 */
  private RouterMeta meta;

  /** 地址 */
  private String url;

  /** 子菜单/权限 */
  private List<VueRouter<T>> children;

  @JsonIgnore private boolean hasParent = false;

  @JsonIgnore private boolean hasChildren = false;

  public void initChildren() {
    this.children = new ArrayList<>();
  }
}
