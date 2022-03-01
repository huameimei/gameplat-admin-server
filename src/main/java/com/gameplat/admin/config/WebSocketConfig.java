package com.gameplat.admin.config;

import com.gameplat.base.common.util.StringUtils;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.jwt.JwtTokenUtil;
import com.gameplat.security.manager.JwtTokenAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private JwtTokenAuthenticationManager tokenAuthenticationManager;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    taskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
    taskScheduler.initialize();

    registry
        .enableSimpleBroker("/topic", "/queue", "/user")
        .setHeartbeatValue(new long[] {10000, 10000})
        .setTaskScheduler(taskScheduler);

    registry.setApplicationDestinationPrefixes("/app");
    registry.setUserDestinationPrefix("/user/");
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    registry.setMessageSizeLimit(10240).setSendBufferSizeLimit(10240).setSendTimeLimit(10000);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);
    registration.interceptors(
        new ChannelInterceptor() {

          @Override
          public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
              String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
              if (StringUtils.isNotBlank(token)) {
                try {
                  accessor.setUser(this.getPrincipal(token));
                } catch (Exception e) {
                  throw new MessagingException("请求未授权或登录凭证已过期!");
                }
                return message;
              }
            }
            return ChannelInterceptor.super.preSend(message, channel);
          }

          private Principal getPrincipal(String token) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserCredential credential =
                tokenAuthenticationManager.getCredentialCacheByUsername(username);

            return jwtTokenUtil.validateToken(token, credential.getUsername())
                ? credential::getUsername
                : null;
          }
        });
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration registration) {
    registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);
  }
}
