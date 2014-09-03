package com.dbolshak.testtask;

import com.dbolshak.testtask.rest.service.TopicService;
import com.dbolshak.testtask.rest.service.TopicServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class JavaConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(JavaConfiguration.class, args);
    }

    @Bean
    public TopicService asTopicService() {
        return new TopicServiceImpl();
    }
}