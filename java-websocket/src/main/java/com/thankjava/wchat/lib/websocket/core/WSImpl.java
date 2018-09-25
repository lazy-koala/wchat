package com.thankjava.wchat.lib.websocket.core;

import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.lib.websocket.Session;
import com.thankjava.wchat.lib.websocket.WS;
import org.java_websocket.WebSocket;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/7
 * @Description:
 **/
final class WSImpl extends Session implements WS {

    private WSImpl() {
    }

    @Override
    public ResponseCode sendMsg(String sessionId, String msgText) {
        WebSocket conn = Session.getConn(sessionId);
        if (conn == null) return ResponseCode.TARGET_OFFLINE;
        conn.send(msgText);
        return ResponseCode.SUCCESS;
    }

    @Override
    public void closeConn(String sessionId) {
        WebSocket conn = Session.getConn(sessionId);
        if (conn == null) return;
        conn.close();
    }

    @Override
    public boolean existConn(String sessionId) {
        return Session.getConn(sessionId) != null;
    }
}
