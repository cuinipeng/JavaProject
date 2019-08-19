package com.github.cuinipeng;

import com.github.cuinipeng.entity.User;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/18 13:25
 * @description 测试 Redis 功能正常
 *  RedisTemplate.opsForValue 对应字符串操作
 *  RedisTemplate.opsForZSet 对应有序集合操作
 *  RedisTemplate.opsForHash 对应哈希操作
 *  RedisTemplate.opsForList 对应列表操作
 *  RedisTemplate.opsForSet 对应集合操作
 *  RedisTemplate.opsForGeo 对应地理位置操作
 *  RedisTemplate.opsForHyperLogLog
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    private static Logger logger = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws InterruptedException {
        // 测试之前清空数据库
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        // 测试线程安全性
        int threadSize = 16;
        String threadKey = "concurrent_cnt";
        ExecutorService service = Executors.newFixedThreadPool(threadSize);
        IntStream.range(0, threadSize).forEach(
            idx -> {
                service.execute(
                    () -> stringRedisTemplate.opsForValue().increment(threadKey, 1)
                );
            }
        );
        // 关闭线程
        service.shutdown();
        // 等待全部子线程结束
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        Assert.assertEquals(threadKey, stringRedisTemplate.opsForValue().get(threadKey));
        logger.info("redis: " + stringRedisTemplate.opsForValue().get(threadKey));

        String key = "user:123";
        redisTemplate.opsForValue().set(key, new User(123, "cuinipeng", "13*******57"));
        User user = (User)redisTemplate.opsForValue().get(key);
        logger.info("redis: " + user.toString());
    }

}
