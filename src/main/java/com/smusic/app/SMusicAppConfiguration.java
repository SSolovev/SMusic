package com.smusic.app;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sergey on 28.05.17.
 */
@org.springframework.context.annotation.Configuration
public class SMusicAppConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setMessageConverters(getConverters());
        return restTemplate;
    }
}
