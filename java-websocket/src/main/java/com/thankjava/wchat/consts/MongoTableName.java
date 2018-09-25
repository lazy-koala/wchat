package com.thankjava.wchat.consts;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public enum MongoTableName {

    user,
    friend_apply,
    friend_relation,
    group_info,
    group_relation,
    group_apply,

    ;
    public String value(){
        return this.name();
    }
}
