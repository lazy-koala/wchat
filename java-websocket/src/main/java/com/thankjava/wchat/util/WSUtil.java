package com.thankjava.wchat.util;

import com.thankjava.toolkit.thread.ThreadPool;
import com.thankjava.toolkit3d.fastjson.FastJson;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.lib.websocket.WS;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 包装之后的简单WS处理方法
 */
public class WSUtil {

    private static WS ws;
    private static ThreadPool threadPool = new ThreadPool();
    static Logger logger = LoggerFactory.getLogger(WSUtil.class);

    /**
     * 同步发起通知
     *
     * @param msgPushContext
     * @return
     */
    public static ResponseCode sendMsg(MsgPushContext msgPushContext) {
        return ws.sendMsg(msgPushContext.getToUserId().concat(";/notice/event"), msgPushContext.toJsonString());
    }

    /**
     * 异步发起通知
     *
     * @param msgPushContext
     */
    public static void sendMsgSync(MsgPushContext msgPushContext) {

        threadPool.execute(() -> {
           ws.sendMsg(msgPushContext.getToUserId().concat(";/notice/event"), msgPushContext.toJsonString());
        });

    }

    public static boolean isOnline(String userId) {
        return ws.existConn(userId.concat(";/notice/event"));
    }

    public static void closeConn(String sessionId) {
        ws.closeConn(sessionId);
    }
}