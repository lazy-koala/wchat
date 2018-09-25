package com.thankjava.wchat.db.entity;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description: 用户表信息
 **/
public class User extends BaseEntity{

    private String username;
    private String password;
    private String nickname;
    private String headImg;
    private String sign;
    private String sex; // -1 - 未设置  1 - 男  0 - 女

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
