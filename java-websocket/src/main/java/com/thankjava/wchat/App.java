package com.thankjava.wchat;

import com.thankjava.wchat.lib.websocket.BasicWebSocket;
import com.thankjava.wchat.ws.core.ConnectionVerifyCallBack;
import com.thankjava.wchat.ws.core.OnConnCloseCallBack;
import com.thankjava.wchat.ws.core.OnMessageCallBack;
import org.java_websocket.WebSocketImpl;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/3
 * @Description:
 **/
public class App {

    public static void main(String[] args) {

        new BasicWebSocket(
                8004,
                new ConnectionVerifyCallBack(),
                new OnMessageCallBack(),
                new OnConnCloseCallBack()
        ).run();
    }

}