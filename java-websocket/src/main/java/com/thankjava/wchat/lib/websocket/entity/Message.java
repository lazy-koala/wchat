package com.thankjava.wchat.lib.websocket.entity;

import com.thankjava.wchat.db.entity.User;
import org.java_websocket.WebSocket;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/9
 * @Description:
 **/
public class Message {

    private ConVerifyResult conVerifyResult;
    private WebSocket conn;
    private String message;

    public Message(ConVerifyResult conVerifyResult, WebSocket conn, String message) {

        this.conVerifyResult = conVerifyResult;
        this.conn = conn;
        this.message = message;

    }

    public String getFromSessionId() {
        return conVerifyResult.getSessionId();
    }

    public String getFromUserId() {
        return conVerifyResult.getUserId();
    }

    public String getMessage() {
        return message;
    }

    public WebSocket getConn() {
        return conn;
    }

    public User getBindData() {
        return (User) conVerifyResult.getBindData();
    }

    /**
     * 执行controller逻辑
     */
    public void doProcess(Object... args) throws InvocationTargetException, IllegalAccessException {
        conVerifyResult.getProcess().invoke(conVerifyResult.getController(), args);
    }
}
