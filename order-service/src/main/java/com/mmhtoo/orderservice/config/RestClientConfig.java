package com.mmhtoo.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient inventoryServiceClient(){
    return RestClient.builder()
        .baseUrl("http://localhost:8083/api/v1/inventories")
        .build();
  }

}
