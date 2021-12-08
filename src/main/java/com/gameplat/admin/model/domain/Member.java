package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 会员
 *
 * @author Lenovo
 */
@Data
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TableName("member")
public class Member extends Model<Member> {

  @TableId(type = IdType.AUTO)
  public Long id;

  @ApiModelProperty(value = "会员名")
  private String account;

  @ApiModelProperty(value = "会员昵称")
  private String nickname;

  @ApiModelProperty(value = "会员层级/代理等级")
  private Integer userLevel;

  /** 代理路径 */
  private String superPath;

  @ApiModelProperty(value = "代理等级")
  private Integer agentLevel;

  private Long parentId;

  @ApiModelProperty(value = "上级名称")
  private String parentName;

  @ApiModelProperty(value = "下级人数")
  private Integer lowerNum;

  @ApiModelProperty(value = "登录密码")
  private String password;

  @ApiModelProperty(value = "密码盐")
  private String salt;

  private String language;

  @ApiModelProperty(value = "密码强度")
  private String passStrength;

  @ApiModelProperty(value = "是否已改密码 0:未改 1:已改")
  private String changeFlag;

  @ApiModelProperty(value = "是否删除 0:未删除  1:已删除")
  private String delFlag;

  private String withdrawFlag;

  private String levelLockFlag;

  @ApiModelProperty(value = "账号状态(0-- 停用，1-- 正常，2 --- 冻结)")
  private Integer status;

  @ApiModelProperty(value = "用户类型")
  private String userType;

  @ApiModelProperty(value = "真实姓名")
  private String realName;

  @ApiModelProperty(value = "电话")
  private String phone;

  @ApiModelProperty(value = "手机号码国际区号")
  private String dialCode;

  @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
  private Integer sex;

  @ApiModelProperty(value = "邮箱")
  private String email;

  @ApiModelProperty(value = "微信")
  private String wechat;

  @ApiModelProperty(value = "QQ")
  private String qq;

  @ApiModelProperty(value = "生日(格式0706)")
  private Date birthday;

  @ApiModelProperty(value = "注册IP")
  private String registerIp;

  @ApiModelProperty(value = "注册来源")
  private Integer registerSource;

  private String registerBrowser;

  private String registerOs;

  @ApiModelProperty(value = "注册域名")
  private String registerHost;

  @ApiModelProperty(value = "认证状态")
  private String authenticateFlag;

  @ApiModelProperty(value = "备注")
  private String remark;

  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  @ApiModelProperty(value = "创建人")
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

  @TableField(exist = false)
  private Integer superAgentLevel;

  private Integer tableIndex;
}
