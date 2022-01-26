package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户对象 sys_user
 *
 * @author three
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUser implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 用户ID */
  @TableId(type = IdType.AUTO)
  private Long userId;

  /** 用户账号 */
  private String userName;

  /** 用户昵称 */
  private String nickName;

  /** 用户类型 */
  private String userType;

  /** 手机号码 */
  private String phone;

  /** 用户头像 */
  private String avatar;

  /** 密码 */
  private String password;

  /** 安全码 */
  private String safeCode;

  /** 帐号状态（1正常 0停用） */
  private Integer status;

  /** 修改密码标志（0没有修改 1已修改） */
  private Integer changeFlag;

  /** 后台个人设置 */
  private String settings;

  /** 限制详情 */
  private String limitInfo;

  /** 最后登录IP */
  private String loginIp;

  /** 最后登录时间 */
  private Date loginDate;

  /** 角色id */
  private Long roleId;

  private String remark;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  @TableField(value = "create_by", fill = FieldFill.INSERT)
  private String createBy;

  @TableField(value = "update_time", fill = FieldFill.UPDATE)
  private Date updateTime;

  @TableField(value = "update_by", fill = FieldFill.UPDATE)
  private String updateBy;

  /** 角色组 */
  @TableField(exist = false)
  private Long[] roleIds;

  /** 用户层级 */
  private String userLevel;
}
