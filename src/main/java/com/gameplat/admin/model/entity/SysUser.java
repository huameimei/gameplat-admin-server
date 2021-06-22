package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/** 管理员实体类
 * @author Lenovo
 * */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

  /** 角色ID */
  private Long roleId;

  /** 会员账号 */
  private String account;

  /** 密码 */
  private String password;

  /** 加盐 */
  private String salt;

  /** 用户昵称 */
  private String nickname;

  /** 谷歌验证码编码 */
  private String googleCode;

  /** 手机号码 */
  private String phone;

  /** 管理会员层级(充值/提现) */
  private String userLevel;

  /** 管理员状态 */
  private Integer state;

  /** 管理员类型 */
  private Integer userType;

  /** 管理员密码错误受限 0正常,1密码错误限制 */
  private Integer limited;

  /** 后台个人设置 */
  private String settings;

  /** 限制信息 子账号 出入款可操作性的限额 */
  private String limitInfo;

}
