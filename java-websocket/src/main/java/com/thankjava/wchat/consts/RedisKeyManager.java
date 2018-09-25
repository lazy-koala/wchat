package com.thankjava.wchat.consts;

/**
 * RedisKey 管理
 */
public class RedisKeyManager {

    private RedisKeyManager() {
    }

    private final static String BASIC_KEY_PREFIX = "wchat";

    /**
     * 创建缓存key
     *
     * @param keys
     * @return
     */
    private static String buildKey(String... keys) {
        if (keys == null || keys.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(BASIC_KEY_PREFIX);
        for (String key : keys) {
            stringBuilder.append(":");
            stringBuilder.append(key);
        }
        return stringBuilder.toString();
    }


    /**
     * cookie 用户 token
     *
     * @param token
     * @return
     */
    public static String TOKEN_KEY(String token) {
        return buildKey("token", token);
    }

    /**
     * 在线用户key集合
     * @return
     */
    public static String ONLINE_SET_KEY() {
        return buildKey("user", "online");
    }
}
