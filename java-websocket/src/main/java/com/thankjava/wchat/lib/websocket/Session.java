package com.thankjava.wchat.lib.websocket;

import com.thankjava.toolkit.bean.thread.TaskEntity;
import com.thankjava.toolkit.core.thread.ThreadTask;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocketImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/3
 * @Description: 所有已建立的有效连接汇总
 **/
public class Session {

    static Logger logger = LoggerFactory.getLogger((Session.class));

    private static final Map<String, WebSocket> aliveConnection = new ConcurrentHashMap<>();

//    static {
//
//        if (WebSocketImpl.DEBUG == true) {
//
//            ThreadTask threadTask = new ThreadTask(1);
//            TaskEntity taskEntity = new TaskEntity(5, 10, () -> {
//                logger.debug("all aliveConnection size = " + aliveConnection.size());
//                for (String sessionId : aliveConnection.keySet()) {
//                    logger.debug("    -> sessionId = " + sessionId);
//                }
//            });
//
//            threadTask.addTaskAtFixedRate(taskEntity);
//        }
//
//    }

    // 新增连接
    protected static void putConn(String sessionId, WebSocket conn) {
        WebSocket currConn = aliveConnection.get(sessionId);
        if (currConn != null) {
            currConn.close();
        }
        aliveConnection.put(sessionId, conn);
    }

    // 获取连接
    protected static WebSocket getConn(String sessionId) {
        return aliveConnection.get(sessionId);
    }

    // 删除连接
    protected static void delConn(String sessionId) {
        WebSocket currConn = aliveConnection.get(sessionId);
        if (currConn != null) {
            currConn.close();
        }
        aliveConnection.remove(sessionId);
    }
}
