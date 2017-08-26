package com.smusic.app;

import com.smusic.app.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sergey on 28.05.17.
 */
@Configuration
public class SMusicAppConfiguration {

    @Value("${global.pool.threads}")
    private int threadsNum;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setMessageConverters(getConverters());
        return restTemplate;
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(someFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("someFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ExecutorService globalExecutorPool() {
        return Executors.newFixedThreadPool(threadsNum);
    }

    public Filter someFilter() {
        return new AuthFilter();
    }
}
