package com.thankjava.wchat.controller;

import com.thankjava.wchat.bean.RequestContext;
import com.thankjava.wchat.ws.anno.WSController;
import com.thankjava.wchat.ws.anno.WSProcess;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
@WSController(path = "notice")
public class Notice {


    @WSProcess(path = "event")
    public void event(RequestContext ctx) {

    }
}
