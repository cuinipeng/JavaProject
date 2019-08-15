package com.github.cuinipeng.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/15 21:22
 * @description Redis NoSQL 基本操作
 */
public class RedisHandler {

    private static Logger logger = LoggerFactory.getLogger(RedisHandler.class);

    // redis 连接池
    private JedisPool redisPool = null;
    // redis cluster
    JedisCluster redisCluster = null;
    // redis 认证
    private boolean redisAuthed = false;
    // 是否是 redis cluster
    private boolean redisClustered = false;
    // redis 配置
    // String host = "127.0.0.1";
    String host = "192.168.100.135";
    int port = 6379;
    int maxTotal = 16;
    int maxIdle = 4;
    int maxWaitMillis = 100;

    public RedisHandler() {
        // 初始化连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        redisPool = new JedisPool(poolConfig, host, port);

        if (redisClustered) {
            // 初始化 redis cluster
            Set<HostAndPort> redisClusterNodes = new HashSet<>();
            redisClusterNodes.add(new HostAndPort(host, port));
            redisCluster = new JedisCluster(redisClusterNodes);
        }
    }

    public void testWriteSpeed() {
        // jedis 对象线程不安全, 可以使用 lettuce 库
        Jedis redis = redisPool.getResource();

        if (redisAuthed) {
            redis.auth("password");
        }

        if (redis == null) {
            logger.warn("can't get redis connection");
            return;
        }

        // 清空数据库
        redis.flushAll();

        long maxSize = 100 * 100;
        long cnt = 0;

        long start = System.currentTimeMillis();
        while (cnt < maxSize) {
            String key = String.format("RedisHandler-%d", cnt);
            String value = String.format("%d", cnt);
            redis.set(key, value);
            cnt++;
        }
        long end = System.currentTimeMillis();
        logger.info(String.format("redis write speed: %.2f r/s", (maxSize * 1000.0 / (end - start))));
    }

    public void testCluster() {
        redisCluster.set("RedisCluster", "https://github.com/xetorthio/jedis");
    }

    public void run() {
        testWriteSpeed();
        // testCluster();
    }
}
