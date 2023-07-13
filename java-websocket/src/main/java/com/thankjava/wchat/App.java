package com.thankjava.wchat;

import com.thankjava.toolkit.core.thread.ThreadUtil;
import com.thankjava.wchat.lib.websocket.BasicWebSocket;
import com.thankjava.wchat.ws.core.ConnectionVerifyCallBack;
import com.thankjava.wchat.ws.core.OnConnCloseCallBack;
import com.thankjava.wchat.ws.core.OnMessageCallBack;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/3
 * @Description:
 **/
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        int port = 80;
        Properties properties = new Properties();
        try {
            FileReader fileReader = new FileReader("./config/system.properties");
            properties.load(fileReader);
            fileReader.close();
            port = Integer.parseInt(properties.getProperty("websocket.port"));
        } catch (Exception e) {
            // ignore
        }
        WebSocketServer webSocketServer = new BasicWebSocket(
                port,
                new ConnectionVerifyCallBack(),
                new OnMessageCallBack(),
                new OnConnCloseCallBack()
        );

        webSocketServer.start();

        ThreadUtil.runWhenJVMExit(() -> {
            try {
                webSocketServer.stop();
                logger.debug("server shutdown");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}