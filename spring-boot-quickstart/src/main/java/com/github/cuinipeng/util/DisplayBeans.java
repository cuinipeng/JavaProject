package com.github.cuinipeng.util;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/18 13:00
 * @description 打印 Spring Boot 注入的 Bean
 */
@Component
public class DisplayBeans implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(DisplayBeans.class);
    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        int idx = 0;
        for(String beanName: beanNames) {
            idx++;
            logger.debug(String.format("%03d %s", idx, beanName));
        }
    }
}
