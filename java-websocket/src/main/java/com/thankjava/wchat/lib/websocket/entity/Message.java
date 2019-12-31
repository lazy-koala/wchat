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

    private VerifiedConnection verifiedConnection;
    private WebSocket conn;
    private String message;

    public Message(VerifiedConnection verifiedConnection, WebSocket conn, String message) {

        this.verifiedConnection = verifiedConnection;
        this.conn = conn;
        this.message = message;

    }

    public String getFromSessionId() {
        return verifiedConnection.getSessionId();
    }

    public String getFromUserId() {
        return verifiedConnection.getUserId();
    }

    public String getMessage() {
        return message;
    }

    public WebSocket getConn() {
        return conn;
    }

    public User getBindData() {
        return (User) verifiedConnection.getBindData();
    }

    /**
     * 执行controller逻辑
     */
    public void doProcess(Object... args) throws InvocationTargetException, IllegalAccessException {
        verifiedConnection.getProcess().invoke(verifiedConnection.getController(), args);
    }
}
