package com.thankjava.wchat.bean;

import com.thankjava.toolkit3d.core.fastjson.FastJson;
import com.thankjava.wchat.consts.ResponseCode;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
public class ResponseContext{

    private String code;
    private String desc;
    private Object data;

    public ResponseContext(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.desc = responseCode.desc;
    }

    public ResponseContext(ResponseCode responseCode, String desc) {
        this.code = responseCode.code;
        this.desc = desc;
    }

    public ResponseContext(ResponseCode responseCode, Object data) {
        this.code = responseCode.code;
        this.desc = responseCode.desc;
        this.data = data;
    }

    public ResponseContext(ResponseCode responseCode, String desc, Object data) {
        this.code = responseCode.code;
        this.desc = desc;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Object getData() {
        return data;
    }

    public String toJsonString() {
        return FastJson.toJSONString(this);
    }
}
