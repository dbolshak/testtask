package com.dbolshak.testtask;

import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class JavaConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(JavaConfiguration.class);
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(JavaConfiguration.class, args);
    }

    @Bean
    TopicDao topicDao() {
        if (Helper.useCache()) {
            LOG.info("returning topicCacheDao as a topicDao");
            return context.getBean("topicCacheDao", TopicDao.class);
        }
        LOG.info("returning fileSystemDao as a topicDao");
        return context.getBean("fileSystemDao", TopicDao.class);
    }
}
