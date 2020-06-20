package com.nammaexpo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nammaexpo")
public class VirtualExpoApplication {

  public static void main(String[] args) {
    SpringApplication.run(VirtualExpoApplication.class, args);
  }

  @Bean
  public ObjectMapper createObjectMapper() {
    return new ObjectMapper();
  }
}