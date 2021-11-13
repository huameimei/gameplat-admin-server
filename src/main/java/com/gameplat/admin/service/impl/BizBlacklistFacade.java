package com.gameplat.admin.service.impl;

import com.gameplat.admin.enums.BlacklistConstant.BizBlacklistType;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.service.BizBlacklistService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class BizBlacklistFacade {
  @Resource private BizBlacklistService bizBlacklistService;

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
