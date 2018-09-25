package com.thankjava.wchat.bean.controller.friend;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description: 好友新增申请
 **/
public class FriendAdd {

    // 添加好友时发送的备注申请
    private String remark;

    // 请求添加的用户id
    private String userId;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
