package com.cjrequena.sample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration
public class OrderServiceApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
  }
}
