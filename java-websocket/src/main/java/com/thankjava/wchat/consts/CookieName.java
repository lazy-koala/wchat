package com.thankjava.wchat.consts;

public enum CookieName {

    TOKEN_KEY("token"),
    UID_KEY("uid");

    CookieName(String code) {
        this.code = code;
    }

    public String code;


}
