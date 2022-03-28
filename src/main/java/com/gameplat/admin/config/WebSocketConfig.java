package com.gameplat.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  public static final String TOPIC_BROKER = "/topic";
  public static final String USER_BROKER = "/user";
  private static final String ENDPOINT = "/websocket";
  private static final String APPLICATION_DESTINATION_PREFIXES = "/app";
  private static final long[] HEARTBEAT = new long[] {10000, 10000};

  @Autowired private AuthChannelInterceptor authChannelInterceptor;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(ENDPOINT).setAllowedOrigins("*").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    taskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
    taskScheduler.initialize();

    registry
            .enableSimpleBroker(TOPIC_BROKER, USER_BROKER)
            .setHeartbeatValue(HEARTBEAT)
            .setTaskScheduler(taskScheduler);

    registry.setApplicationDestinationPrefixes(APPLICATION_DESTINATION_PREFIXES);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);
    registration.interceptors(authChannelInterceptor);
  }

  @Override
  public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
    registry.setMessageSizeLimit(10240).setSendBufferSizeLimit(10240).setSendTimeLimit(10000);
  }

  @Override
  public void configureClientOutboundChannel(ChannelRegistration registration) {
    registration.taskExecutor().corePoolSize(10).maxPoolSize(20).keepAliveSeconds(60);
  }
}
