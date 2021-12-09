package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;
import org.dozer.Mapping;

/**
 * 用户信息
 * @author three
 */
@Data
public class UserInfoVo {

  /**
   * id
   */
  @Mapping(value = "userId")
  private Long id;
  /**
   * 角色id
   */
  private Long roleId;
  /**
   * 账号
   */
  @Mapping(value = "userName")
  private String loginName;
  /**
   * 昵称
   */
  private String nickName;
  /**
   * 用户类型
   */
  private Integer userType;
  /**
   * 电话
   */
  private String phone;
  /**
   * 头像
   */
  private String avatar;
  /**
   * 状态
   */
  private Integer status;
  /**
   * 最新登录ip
   */
  private String loginIp;
  /**
   * 最新登录时间
   */
  private Date loginDate;
  /**
   * 创建时间
   */
  private Date createTime;
}
