package com.thankjava.wchat.db.entity;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description: 好友关系中间信息
 **/
public class FriendRelation extends BaseEntity {

    private String userId;
    private String friendUserId;
    private String remark;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
