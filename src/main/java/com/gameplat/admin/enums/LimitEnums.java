package com.gameplat.admin.enums;

import com.gameplat.admin.model.domain.limit.*;
import com.gameplat.common.exception.ServiceException;

import java.util.Arrays;

public enum LimitEnums {
  ADMIN_LOGIN_LIMIT("adminLoginLimit", LoginLimit.class),

  MEMBER_LOGIN_LIMIT("memberLoginLimit", LoginLimit.class),

  MEMBER_REGISTRY_LIMIT("memberRegistryLimit", MemberRegistryLimit.class),

  MEMBER_WITHDRAW_LIMIT("memberWithdrawLimit", MemeberWithdrawLimit.class),

  LIVE_TRANSFER_LIMIT("liveTransferLimit", LiveTransferLimit.class),

  MEMBER_RECHARGE_LIMIT("memberRechargeLimit", MemberRechargeLimit.class),

  EDIT_USER_INFO_LIMIT("editUserInfoLimit", EditUserInfoLimit.class);

  private String name;

  private Class value;

  LimitEnums(String name, Class value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Class getValue() {
    return value;
  }

  public static Class<?> getClass(String name) throws ServiceException {
    return Arrays.stream(LimitEnums.values())
        .filter(limitEnum -> limitEnum.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new ServiceException("配置信息不存在!"))
        .value;
  }

  public static LimitEnums getName(String name) {
    for (LimitEnums v : LimitEnums.values()) {
      if (v.name.equals(name)) {
        return v;
      }
    }
    return null;
  }
}
