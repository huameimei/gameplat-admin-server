package com.gameplat.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author robben
 */
@Configuration
public class ThreadPoolConfig {

  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

  private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

  private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;

  private static final int QUEUE_CAPACITY = 20;

  private static final int KEEP_ALIVE_TIME = 30;

  @Bean("asyncExecutor")
  public Executor asyncExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
    taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);

    taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
    taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
    taskExecutor.setThreadNamePrefix("default-task-executor-");

    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.setTaskDecorator(DelegatingSecurityContextRunnable::new);
    taskExecutor.initialize();
    return taskExecutor;
  }

  @Bean("asyncGameExecutor")
  @Primary
  public Executor asyncGameExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
    taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);

    taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
    taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
    taskExecutor.setThreadNamePrefix("game-task-executor-");

    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.setTaskDecorator(DelegatingSecurityContextRunnable::new);
    taskExecutor.initialize();
    return taskExecutor;
  }

  /**
   * Spring security并发支持<br>
   * 参见：<a
   * href="https://github.com/spring-projects/spring-security/issues/6856#issuecomment-518787966">
   * https://github.com/spring-projects/spring-security/issues/6856#issuecomment-518787966 </a>
   *
   * @param delegate ThreadPoolTaskExecutor
   * @return DelegatingSecurityContextAsyncTaskExecutor
   */
  @Bean
  public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) {
    return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
  }
}
