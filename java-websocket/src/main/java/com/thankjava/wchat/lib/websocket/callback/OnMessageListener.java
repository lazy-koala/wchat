package com.thankjava.wchat.lib.websocket.callback;

import com.thankjava.wchat.lib.websocket.entity.Message;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/9
 * @Description: 接受到消息时触发
 **/
public interface OnMessageListener {

    void doProcess(Message message);
}
