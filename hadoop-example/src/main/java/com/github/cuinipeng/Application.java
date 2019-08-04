package com.github.cuinipeng;

import com.github.cuinipeng.hbase.HBaseService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        testHbaseService();
    }

    public static void testHbaseService() {
        new HBaseService().run();
    }

    public static void testNewDateTimeAPI() {
        // ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        System.out.println(localDateTime);
        System.out.println(zonedDateTime);
    }
}
