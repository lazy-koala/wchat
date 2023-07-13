package com.thankjava.wchat.lib.websocket.callback;

import com.thankjava.wchat.lib.websocket.entity.VerifiedConnection;

public interface OnConnCloseListener {

    void doProcess(VerifiedConnection<?> verifiedConnection);
}
