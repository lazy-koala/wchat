package com.thankjava.wchat.lib.websocket;

import com.thankjava.wchat.consts.ResponseCode;
import org.java_websocket.WebSocket;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/6
 * @Description:
 **/
public interface WS {

    /**
     * 向指定用户发送 WebSocket 文本信息
     *
     * @param sessionId
     * @param msgText
     * @return
     */
    ResponseCode sendMsg(String sessionId, String msgText);

    /**
     * 关闭连接
     * @param sessionId
     */
    void closeConn(String sessionId);

    /**
     * 判断连接是否存在有效
     * @param sessionId
     * @return
     */
    boolean existConn(String sessionId);

}
