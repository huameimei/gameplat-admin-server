package com.gameplat.admin.event.listener;

import com.gameplat.admin.cache.AdminCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 登录成功事件处理
 *
 * @author robben
 */
@Component
public class AuthenticationSuccessEventListener
    implements ApplicationListener<AuthenticationSuccessEvent> {

  @Autowired private AdminCache adminCache;

  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent event) {
    String username = event.getAuthentication().getName();
    adminCache.cleanErrorPasswordCount(username);
  }
}
