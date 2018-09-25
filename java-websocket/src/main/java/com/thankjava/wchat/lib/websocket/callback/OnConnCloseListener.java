package com.thankjava.wchat.lib.websocket.callback;

import com.thankjava.wchat.lib.websocket.entity.ConVerifyResult;

public interface OnConnCloseListener {

    void doProcess(ConVerifyResult conVerifyResult);
}
