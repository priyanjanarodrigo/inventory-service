package com.myorg.is.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for management-related beans.
 */
@Configuration
public class ManagementConfiguration {

  /**
   * Bean definition for HttpExchangeRepository using in-memory storage.
   *
   * @return an instance of InMemoryHttpExchangeRepository
   */
  @Bean(name = "httpTraceRepository")
  public HttpExchangeRepository httpTraceRepository() {
    return new InMemoryHttpExchangeRepository();
  }
}
