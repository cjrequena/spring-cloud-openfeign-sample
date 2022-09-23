package com.cjrequena.sample.configuration;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@EnableFeignClients
public class FeignClientConfiguration {

  private ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;

  @Bean
  Encoder feignFormEncoder() {
    return new SpringFormEncoder(new SpringEncoder(messageConverters));
  }

  @Bean
  Decoder feignFormDecoder() {
    return new SpringDecoder(messageConverters);
  }
}
