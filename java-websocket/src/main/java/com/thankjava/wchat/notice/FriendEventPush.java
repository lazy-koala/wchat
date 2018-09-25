package com.thankjava.wchat.notice;

import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.notice.friend.FriendAddPush;
import com.thankjava.wchat.bean.notice.friend.FriendAddReplyPush;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.util.WSUtil;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public class FriendEventPush {


    /**
     * 推送请求添加好友
     *
     * @param msgPushContext
     * @return
     */
    public ResponseContext pushFriendAdd(MsgPushContext<FriendAddPush> msgPushContext) {
        ResponseCode responseCode = WSUtil.sendMsg(msgPushContext);
        return new ResponseContext(responseCode);
    }

    public ResponseContext pushFriendAddReply(MsgPushContext<FriendAddReplyPush> msgPushContext) {
        WSUtil.sendMsg(msgPushContext);
        return new ResponseContext(ResponseCode.SUCCESS);
    }

    public ResponseContext pushFriendDel(MsgPushContext msgPushContext) {
        ResponseCode responseCode = WSUtil.sendMsg(msgPushContext);
        if (responseCode == ResponseCode.TARGET_OFFLINE) {
            return new ResponseContext(ResponseCode.SUCCESS);
        }
        return new ResponseContext(responseCode);
    }
}
