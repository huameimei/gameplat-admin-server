package com.gameplat.admin.config;

import com.gameplat.base.common.util.StringUtils;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.jwt.JwtTokenUtil;
import com.gameplat.security.manager.JwtTokenAuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * websocket 鉴权拦截器
 *
 * @author robben
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class AuthChannelInterceptor implements ChannelInterceptor {

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private JwtTokenAuthenticationManager tokenAuthenticationManager;

  @Override
  public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (this.isFirstConnect(accessor)) {
      this.setPrincipal(accessor);
    }
    return message;
  }

  private void setPrincipal(StompHeaderAccessor accessor) {
    String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isNotBlank(token)) {
      try {
        accessor.setUser(this.getPrincipal(token));
      } catch (Exception e) {
        log.error("Websocket鉴权失败: {}", e.getMessage());
        throw new MessagingException("请求未授权或登录凭证已过期!");
      }
    }
  }

  private boolean isFirstConnect(StompHeaderAccessor accessor) {
    return null != accessor && StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private Principal getPrincipal(String token) {
    UserCredential userCredential = tokenAuthenticationManager.getCredentialCacheByToken(token);
    if (null == userCredential) {
      return null;
    }

    return jwtTokenUtil.validateToken(token, userCredential.getUsername())
        ? userCredential::getUsername
        : null;
  }
}
