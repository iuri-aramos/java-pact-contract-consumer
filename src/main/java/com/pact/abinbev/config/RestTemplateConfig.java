package com.pact.abinbev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate createRestTemplate(RequestLoggingInterceptor loggingInterceptor) {
    final RestTemplate restTemplate =
        new RestTemplate(
            new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    restTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));
    return restTemplate;
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("poolScheduler");
    scheduler.setPoolSize(50);
    return scheduler;
  }
}
