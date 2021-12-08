package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

/**
 * 用户Vo
 *
 * @author three
 */
@Data
public class UserVo {

  /** id */
  private Long id;

  /** 角色ID */
  private Long roleId;

  /** 会员账号 */
  private String account;

  /** 用户昵称 */
  private String nickName;

  /** 手机号码 */
  private String phone;

  /** 管理会员层级(充值/提现) */
  private String userLevel;

  /** 管理员状态 */
  private Integer status;

  /** 管理员类型 */
  private String userType;

  /** 管理员密码错误受限 0正常,1密码错误限制 */
  private Integer limited;

  /** 后台个人设置 */
  private String settings;

  /** 限制信息 子账号 出入款可操作性的限额 */
  private String limitInfo;
  /** 最新登录ip */
  private String loginIp;
  /** 最新登录时间 */
  private Date loginDate;
  /** 创建时间 */
  private Date createTime;
  /** 修改时间 */
  private Date updateTime;
}
