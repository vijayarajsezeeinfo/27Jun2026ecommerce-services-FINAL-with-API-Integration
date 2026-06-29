package com.ezeeinfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;

@Component
public class FeignConfig {

	@Bean
	public ProductFeignClient productFeignClient() {
		return Feign.builder().client(new OkHttpClient()).encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
				.target(ProductFeignClient.class, "http://localhost:8080/ecommerce-services");
	}

}
