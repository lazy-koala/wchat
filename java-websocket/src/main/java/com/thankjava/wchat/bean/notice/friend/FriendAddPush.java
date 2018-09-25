package com.thankjava.wchat.bean.notice.friend;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public class FriendAddPush {

    // 好友申请记录id
    private String friendApplyId;

    private String nickname;
    private String headImg;
    private String username;
    private String remark;
    private String sign;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFriendApplyId() {
        return friendApplyId;
    }

    public void setFriendApplyId(String friendApplyId) {
        this.friendApplyId = friendApplyId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
