package com.thankjava.wchat.db.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public class BaseEntity {

    @JSONField(name = "_id")
    private String id;

    private Long createTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
