package com.thankjava.wchat.bean;

import com.thankjava.toolkit3d.fastjson.FastJson;
import com.thankjava.wchat.consts.EventType;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
public class MsgPushContext<T> {


    private String fromUserId;
    private String toUserId;
    private EventType eventType;
    private T data;
    private Long time = System.currentTimeMillis();

    public MsgPushContext(EventType eventType, String fromUserId, String toUserId) {
        this.eventType = eventType;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    /**
     *
     * @param eventType
     * @param fromUserId 消息来着谁
     * @param toUserId 推送目标
     * @param data
     */
    public MsgPushContext(EventType eventType, String fromUserId, String toUserId, T data) {
        this.eventType = eventType;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.data = data;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String toJsonString() {
        return FastJson.toJSONString(this);
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Long getTime() {
        return time;
    }
}
