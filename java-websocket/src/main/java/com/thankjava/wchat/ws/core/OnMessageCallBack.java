package com.thankjava.wchat.ws.core;

import com.alibaba.fastjson.JSONObject;
import com.thankjava.toolkit3d.core.fastjson.FastJson;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.lib.websocket.callback.OnMessageListener;
import com.thankjava.wchat.lib.websocket.entity.Message;
import com.thankjava.wchat.bean.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/9
 * @Description:
 **/
public class OnMessageCallBack implements OnMessageListener {

    static Logger logger = LoggerFactory.getLogger(OnMessageCallBack.class);

    @Override
    public void doProcess(Message message) {
        try {
            JSONObject data;
            try {
                data = FastJson.toJSONObject(message.getMessage());
            } catch (Exception e) {
                logger.error("ws 请求参数异常 sessionId = " + message.getFromSessionId() + " | msg = " + message.getMessage());
                message.getConn().send(new ResponseContext(ResponseCode.BAD_REQUEST_PARAMETER).toJsonString());
                return;
            }
            logger.debug("get message: " + message.getMessage());
            message.doProcess(new RequestContext(
                            message.getFromUserId(),
                            message.getConn(),
                            message.getBindData(),
                            data
                    )
            );
        } catch (Exception e) {
            logger.error("ws 执行异常 sessionId = " + message.getFromSessionId() + " | msg = " + message.getMessage(), e);
            message.getConn().send(new ResponseContext(ResponseCode.FAILED).toJsonString());
        }
    }
}