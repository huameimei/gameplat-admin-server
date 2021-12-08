package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 菜单权限表 sys_menu
 *
 * @author three
 */
@Data
@ApiModel(value = "菜单权限")
public class SysMenu extends Model<SysMenu> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "菜单ID")
  @TableId(type = IdType.AUTO)
  private Long menuId;

  /** 菜单名称 */
  @ApiModelProperty(value = "菜单名称")
  private String menuName;

  /** 菜单标题 */
  @ApiModelProperty(value = "菜单标题")
  private String title;

  /** 父菜单ID */
  @ApiModelProperty(value = "父菜单ID")
  private Long parentId;

  /** 父菜单名称 */
  @ApiModelProperty(value = "父菜单名称")
  private String parentName;

  /** 显示顺序 */
  @ApiModelProperty(value = "排序值")
  private Integer menuSort;

  /** Web前端组件 */
  @ApiModelProperty(value = "前端组件")
  private String component;

  /** 路径 */
  @ApiModelProperty(value = "页面路径")
  private String path;

  /** 菜单URL */
  @ApiModelProperty(value = "资源链接url")
  private String url;

  /** 类型:0目录,1菜单,2按钮 */
  @ApiModelProperty(value = "类型: M目录,C菜单,B按钮")
  private String menuType;

  /** 0显示1隐藏 */
  @ApiModelProperty(value = "菜单状态")
  private Integer visible;

  /** 是否外链菜单 (0、否1、是) */
  @ApiModelProperty(value = "是否外链菜单")
  private Integer iFrame;

  /** 是否缓存菜单 1缓存 */
  @ApiModelProperty(value = "是否缓存菜单")
  private Integer cacheFlag;

  /** 权限字符串 */
  @ApiModelProperty(value = "权限字符串")
  private String perms;

  /** 菜单图标 */
  @ApiModelProperty(value = "图标")
  private String icon;

  /** 菜单状态:1启用 0禁用 */
  @ApiModelProperty(value = "菜单状态: 1启用 0禁用")
  private Integer status;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(value = "create_by", fill = FieldFill.INSERT)
  private String createBy;

  @TableField(value = "update_time", fill = FieldFill.UPDATE)
  private Date updateTime;

  @TableField(value = "update_by", fill = FieldFill.UPDATE)
  private String updateBy;

  /** 子菜单 */
  @ApiModelProperty(value = "子菜单")
  @TableField(exist = false)
  private List<SysMenu> children = new ArrayList<SysMenu>();
}
