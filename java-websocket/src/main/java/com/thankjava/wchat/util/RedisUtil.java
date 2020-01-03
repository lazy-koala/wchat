package com.thankjava.wchat.util;


import com.thankjava.toolkit3d.core.db.BasicDBManagerBuilder;
import com.thankjava.toolkit3d.core.db.redis.RedisManager;

public class RedisUtil {

    private final static RedisManager REDIS_MANAGER = BasicDBManagerBuilder.buildRedisManager("./config/redis.properties");

    public static RedisManager getRedisManager() {
        return REDIS_MANAGER;
    }
}
