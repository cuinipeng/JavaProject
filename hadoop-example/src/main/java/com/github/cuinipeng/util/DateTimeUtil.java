package com.github.cuinipeng.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/24 21:57
 * @description  Java 8 datetime api
 */
public class DateTimeUtil {
    public void run() {
        // ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        System.out.println(localDateTime);
        System.out.println(zonedDateTime);
    }
}
