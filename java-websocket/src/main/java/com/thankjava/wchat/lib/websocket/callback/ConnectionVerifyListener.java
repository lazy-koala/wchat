package com.thankjava.wchat.lib.websocket.callback;

import com.thankjava.wchat.lib.websocket.entity.VerifiedConnection;
import org.java_websocket.handshake.ClientHandshake;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/6
 * @Description: 连接验证
 **/
public interface ConnectionVerifyListener {

    /**
     * doProcess
     * @param handshake
     * @return
     */
    VerifiedConnection<?> doProcess(ClientHandshake handshake);

}