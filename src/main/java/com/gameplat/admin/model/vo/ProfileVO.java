package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用戶信息Vo
 *
 * @author three
 */
@Data
public class ProfileVO {

  /** 用户名称 */
  private String userName;

  /** 昵称 */
  private String nickName;

  /** 是否修改密码 */
  private Integer isChange;

  /** 电话 */
  private String phone;

  /** 权限列表 */
  private List<String> permis;

  /** 用户个性设置 */
  private String settings;

  /** 创建时间 */
  private Date createTime;

  private String userLevel;

  private String limitInfo;

  private String userType;

  /** 最后登录ip */
  private String loginIp;

  /** 最后登录时间 */
  private Date loginDate;
}
