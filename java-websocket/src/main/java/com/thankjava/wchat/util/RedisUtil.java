package com.thankjava.wchat.util;


import com.thankjava.toolkit3d.core.db.redis.RedisManager;
import com.thankjava.toolkit3d.core.db.redis.datasource.RedisManagerImpl;

public class RedisUtil {

    private final static RedisManager redisManager = RedisManagerImpl.getSingleton("./config/redis.properties");

    public static RedisManager getRedisManager() {
        return redisManager;
    }
}
