package com.example.githubtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubTaskApplication.class, args);
    }

}
