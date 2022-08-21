package com.gameplat.admin.service.impl;

import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.service.BizBlacklistService;
import com.gameplat.model.entity.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("BizBlacklistFacade")
public class BizBlacklistFacade {

  @Autowired private BizBlacklistService bizBlacklistService;

  public boolean isUserInBlacklist(Long userId, BizBlacklistType bizBlacklistType) {
    return isUserInBlacklist(
        bizBlacklistService.getBizBlacklistTypesByUserId(userId), bizBlacklistType);
  }

  public boolean isUserInBlacklist(Member userInfo, BizBlacklistType bizBlacklistType) {
    return isUserInBlacklist(
        bizBlacklistService.getBizBlacklistTypesByUser(userInfo), bizBlacklistType);
  }

  private boolean isUserInBlacklist(
      Set<String> bizBlacklistTypes, BizBlacklistType bizBlacklistType) {
    return bizBlacklistTypes != null
        && bizBlacklistType != null
        && bizBlacklistTypes.contains(bizBlacklistType.getValue());
  }
}
