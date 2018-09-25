package com.thankjava.wchat.lib.websocket.entity;

import java.lang.reflect.Method;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/6
 * @Description:验证连接动作的返回处理结果
 **/
public class ConVerifyResult<T> {

    public ConVerifyResult() {

    }

    public ConVerifyResult(String userId, String path, T bindData, Method process, Object controller) {
        this.allowConnect = true;
        this.userId = userId;
        this.path = path;
        this.bindData = bindData;
        this.sessionId = userId.concat(";").concat(path);
        this.process = process;
        this.controller = controller;
    }

    /**
     * ws对应controller处理方法
     */
    public Method process;

    // 是否允许进行WebSocket连接
    private boolean allowConnect = false;

    private String userId;

    // 当前连接建立的请求地址
    private String path;

    // 全局唯一sessionId (每个ws连接唯一)
    private String sessionId;

    // 为认证成功的连接绑定数据
    private T bindData;

    private Object controller;


    public boolean isAllowConnect() {
        return allowConnect;
    }

    public T getBindData() {
        return bindData;
    }

    public String getPath() {
        return path;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Method getProcess() {
        return process;
    }

    public Object getController() {
        return controller;
    }

    public String getUserId() {
        return userId;
    }
}
