package com.gameplat.admin.event.listener;

import com.gameplat.admin.cache.AdminCache;
import com.gameplat.base.common.util.StringUtils;
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

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    String username = event.getAuthentication().getName();
    if (StringUtils.isNotEmpty(username)) {
      adminCache.updateErrorPasswordCount(username);
    }
  }
}
