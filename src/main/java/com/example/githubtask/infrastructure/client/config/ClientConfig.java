package com.example.githubtask.infrastructure.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
//
//    @Bean
//    public ErrorDecoder feignErrorDecoder(JacksonDecoder decoder, ObjectMapper objectMapper) {
//        return new GithubClientErrorDecoder(decoder, objectMapper);
//    }

}

