package com.github.cuinipeng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S E z");

    // @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "*/30 * * * * *")     // second minute hour day month week
    public void reportCurrentTime() {
        logger.info("The current datetime is {}", dateFormat.format(new Date()));
    }
}
