package com.thankjava.wchat.util;


import com.thankjava.toolkit3d.core.db.BasicDBManagerBuilder;
import com.thankjava.toolkit3d.core.db.redis.RedisManager;

public class RedisUtil {

    private final static RedisManager redisManager = BasicDBManagerBuilder.buildRedisManager("./config/redis.properties");

    public static RedisManager getRedisManager() {
        return redisManager;
    }
}
