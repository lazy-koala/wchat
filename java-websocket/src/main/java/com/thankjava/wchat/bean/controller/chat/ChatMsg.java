package com.thankjava.wchat.bean.controller.chat;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
public class ChatMsg {

    private String toUserId;
    private String toGroupId;
    private String msg;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
