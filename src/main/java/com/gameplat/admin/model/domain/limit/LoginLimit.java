package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginLimit  implements Serializable {
  /**
   * 密码错误次数
   */
  private Integer errorPwdCount;
  /**
   * 是否需要验证码
   */
  private Integer vCode;

  /**
   * 0表示当天不能登录, 1表示必须由后台管理员解除限制
   */
  private Integer errorPwdLimit;

  private String userPasswordType;

  /**
   * 是否开启初始化密码功能，1表示开启，0表示关闭
   * 用于旧平台导入会员时，密码加密策略不一致的情况
   * 导入时将密码字段设置为空，会员第一次登录使用的密码将被作为密码存入数据库
   */
  private Integer initPwdIfEmpty;

  /**
   * 开启初始化密码功能时校验的信息
   */
  private String initPwdVerifyFields;
  /**
   * 开启初始化密码功能时校验的信息至少需通过几项
   */
  private Integer initPwdVerifyCount;

  /**
   * 是否开启ip多帐号登录限制（24小时）
   */
  private Integer limitIpForMultipleAccount;

  /**
   * 每个ip允许的最大登录帐号数量
   */
  private Integer maxAllowedAccountPerIp;

  private String overMaxAllowedAccountTips;

  private Integer ipLocationCode; //ip所属地

  private Integer ipLocationAccountNotLogin;// 0 否 1 是 是否开启未登录账号登录视为归属地变更

  private Integer ipLocationChangeAllowLogin;// 1 禁止登录 0 允许

  private String ipLocationMsg;// ip归属地变更禁止登录提示语

  private String ipLocationLink;//ip归属地变更禁止登录后跳转链接

  private Integer ipLocationVerifyTurnOn;//IP归属地变更认证选项 0禁止登录提示 1 禁止登录后自动认证

  private String ipLocationVerifyFields;//认证选项

  private Integer ipLocationVerifyCount;//至少认证的选项个数

  /**
   * 密码错误次数超限提示信息
   */
  private String errorPwdOverTimesMsg;

  /**
   * 停用账号登录提示信息
   */
  private String accountStateErrorMsg;
  //只允许绑定的手机号登陆 0，否 1 是
  private Integer onlyLoginPhone;
  //允许会员绑定手机号登陆 0，否 1 是
  private Integer loginPhone;
}
