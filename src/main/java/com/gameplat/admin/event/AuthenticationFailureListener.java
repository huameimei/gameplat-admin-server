package com.gameplat.admin.event;

import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * 登录失败事件监听，用于检查密码错误次数
 *
 * @author robben
 */
@Component
public class AuthenticationFailureListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  @Autowired private AdminCache adminCache;

  @Autowired private LimitInfoService limitInfoService;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    String username = event.getAuthentication().getName();
    if (StringUtils.isEmpty(username)) {
      return;
    }

    AdminLoginLimit limit = limitInfoService.getAdminLimit();
    int errorCount = adminCache.getErrorPasswordCount(username);
    if (errorCount < limit.getPwdErrorCount()) {
      // 更新密码错误次数
      adminCache.updateErrorPasswordCount(username, 1);
    }
  }
}
