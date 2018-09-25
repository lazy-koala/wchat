package com.thankjava.wchat.bean.controller.friend;

/**
 * 处理好友申请
 */
public class FriendAddReply {

    private String friendApplyId;
    private Boolean agree;

    public String getFriendApplyId() {
        return friendApplyId;
    }

    public void setFriendApplyId(String friendApplyId) {
        this.friendApplyId = friendApplyId;
    }

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }
}
