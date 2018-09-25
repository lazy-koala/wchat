package com.thankjava.wchat.bean;

import com.alibaba.fastjson.JSONObject;
import com.thankjava.toolkit3d.fastjson.FastJson;
import com.thankjava.wchat.db.entity.User;
import org.java_websocket.WebSocket;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/9
 * @Description:
 **/
public class RequestContext {

    // 消息来源于哪一个用户的userId
    private String fromUserId;
    private User authUser;
    private WebSocket conn;
    private JSONObject data;

    public RequestContext(String fromUserId, WebSocket conn, User authUser, JSONObject data) {
        this.fromUserId = fromUserId;
        this.conn = conn;
        this.authUser = authUser;
        this.data = data;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public <T> T getData(Class<T> tClass) {
        try {
            return data.toJavaObject(tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public User getAuthUser() {
        return authUser;
    }

    /**
     * 向该请求返回数据
     *
     * @param responseContext
     */
    public void response(ResponseContext responseContext) {
        conn.send(responseContext.toJsonString());
    }
}
