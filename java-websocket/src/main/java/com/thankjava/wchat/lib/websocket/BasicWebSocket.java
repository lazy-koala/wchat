package com.thankjava.wchat.lib.websocket;

import com.thankjava.toolkit.reflect.ReflectHelper;
import com.thankjava.wchat.lib.websocket.callback.OnConnCloseListener;
import com.thankjava.wchat.lib.websocket.callback.OnMessageListener;
import com.thankjava.wchat.lib.websocket.entity.ConVerifyResult;
import com.thankjava.wchat.lib.websocket.callback.ConnectionVerifyListener;
import com.thankjava.wchat.lib.websocket.entity.Message;
import com.thankjava.wchat.util.WSUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/3
 * @Description: WebSocket核心封装
 **/
public class BasicWebSocket extends WebSocketServer {

    static Logger logger = LoggerFactory.getLogger(BasicWebSocket.class);

    // 验证WebSocket连接 初始链接数据信息
    private ConnectionVerifyListener connectionVerifyListener;
    private OnMessageListener onMessageListener;
    private OnConnCloseListener onConnCloseListener;

    public BasicWebSocket(int port, ConnectionVerifyListener connectionVerifyListener, OnMessageListener onMessageListener, OnConnCloseListener onConnCloseListener) {
        super(new InetSocketAddress(port));
        if (connectionVerifyListener == null || onMessageListener == null || onConnCloseListener == null) throw new RuntimeException();
        this.connectionVerifyListener = connectionVerifyListener;
        this.onMessageListener = onMessageListener;
        this.onConnCloseListener = onConnCloseListener;
        logger.info("WebSocket Listened " + port);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        ConVerifyResult conVerifyResult = connectionVerifyListener.doProcess(handshake);
        if (conVerifyResult == null) throw new RuntimeException("ConVerifyResult can not be null");
        if (conn == null) {
            logger.error("ws connected but conn is null");
            return;
        }
        if (conVerifyResult.isAllowConnect()) {
            Session.putConn(conVerifyResult.getSessionId(), conn);
            conn.setAttachment(conVerifyResult);
            logger.info("ws connected host = " + conn.getRemoteSocketAddress().getHostString() + " sessionId = " + conVerifyResult.getSessionId());
        } else {
            logger.info("ws refuse path = " + conn.getResourceDescriptor() + " host = " + conn.getRemoteSocketAddress().getHostString());
            conn.close(1008, "Unauthorized request");
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (conn == null) return;
        ConVerifyResult conVerifyResult = conn.<ConVerifyResult>getAttachment();
        if (conVerifyResult == null) return;
        logger.debug("conn closing sessionId = " + conVerifyResult.getSessionId());
        Session.delConn(conVerifyResult.getSessionId());
        onConnCloseListener.doProcess(conVerifyResult);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        ConVerifyResult conVerifyResult = conn.getAttachment();
        logger.debug("onMessage sessionId = " + conVerifyResult.getSessionId() + " message = " + message);
        onMessageListener.doProcess(new Message(conVerifyResult, conn, message));
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        logger.debug("received byte buffer data");
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        logger.error("websocket error", e);
    }

    @Override
    public void onStart() {
        try {
            Class clazz = Class.forName("com.thankjava.wchat.lib.websocket.core.WSImpl");
            Constructor constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            ReflectHelper.setFiledVal(WSUtil.class, "ws", constructor.newInstance());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
