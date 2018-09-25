package com.thankjava.wchat.bean.notice.chat;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
public class ChatMsgPush {

    private String toGroupId;
    private String msg;


    public ChatMsgPush(String toGroupId, String msg) {
        this.toGroupId = toGroupId;
        this.msg = msg;
    }

    public ChatMsgPush(String msg) {
        this.msg = msg;
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
