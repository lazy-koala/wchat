package com.thankjava.wchat.util;

import com.thankjava.toolkit3d.db.redis.RedisManager;
import com.thankjava.toolkit3d.db.redis.datasource.JedisManager;

public class RedisUtil {

    private final static RedisManager redisManager = JedisManager.getSingleton("./config/redis.properties");

    public static RedisManager getRedisManager() {
        return redisManager;
    }
}
