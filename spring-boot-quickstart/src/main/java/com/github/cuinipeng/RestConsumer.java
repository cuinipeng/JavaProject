package com.github.cuinipeng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestConsumer implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(RestConsumer.class);
    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Override
    public void run(String... args) throws Exception {
        String url = "https://gturnquist-quoters.cfapps.io/api/random";
        //String body = restTemplate.getForObject(url, String.class);
        //logger.info(body);
    }
}
