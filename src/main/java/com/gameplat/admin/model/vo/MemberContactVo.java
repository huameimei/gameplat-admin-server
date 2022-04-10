package com.gameplat.admin.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/4/10 23:40 @Version 1.0
 */
@Data
@Builder
public class MemberContactVo implements Serializable {

  /**
   * 真实姓名
   */
  private String realName;

  /**
   * qq
   */
  private String qq;

  /**
   * / 邮箱
   */
  private String email;

  /**
   * 手机号
   */
  private String phone;

  /**
   * 微信
   */
  private String wechat;
}
