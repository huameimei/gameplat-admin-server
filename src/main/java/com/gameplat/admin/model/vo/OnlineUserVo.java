package com.gameplat.admin.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 在线用户VO
 *
 * @author three
 */
@Data
@Builder
public class OnlineUserVo implements Serializable {

  private String username;

  private String nickname;

  private String realName;

  private String parentName;

  private String userType;

  private BigDecimal balance;

  private Date lastLoginDate;

  private String loginIp;

  private String clientType;

  private String deviceType;
}
