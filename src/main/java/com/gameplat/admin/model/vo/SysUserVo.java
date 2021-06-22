package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysUserVo implements Serializable {

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

  private String roleName;

  /**
   * 存款审核限额
   */
  private Double maxRechargeAmountAudit;

  /**
   * 取款审核限额
   */
  private Double maxWithdrawAmountAudit;

  /**
   * 人工存款限额
   */
  private Double maxManualRechargeAmountAudit;

  /**
   * 人工取款限额
   */
  private Double maxManualWithdrawAmountAudit;


}
