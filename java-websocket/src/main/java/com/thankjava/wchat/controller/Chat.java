package com.thankjava.wchat.controller;

import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.controller.chat.ChatMsg;
import com.thankjava.wchat.bean.notice.chat.ChatMsgPush;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.db.entity.FriendRelation;
import com.thankjava.wchat.db.mongo.FriendRelationMapper;
import com.thankjava.wchat.db.mongo.impl.FriendRelationMapperImpl;
import com.thankjava.wchat.notice.ChatEventPush;
import com.thankjava.wchat.ws.anno.WSController;
import com.thankjava.wchat.ws.anno.WSProcess;
import com.thankjava.wchat.bean.RequestContext;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/9
 * @Description: 处理发送及时聊天信息
 **/
@WSController(path = "chat")
public class Chat {

    private ChatEventPush chatEventPush = new ChatEventPush();
    private FriendRelationMapper friendRelationMapper = new FriendRelationMapperImpl();

    @WSProcess(path = "send_msg")
    public void sendMsg(RequestContext ctx) {

        ChatMsg chatMsg = ctx.getData(ChatMsg.class);

        String toUserId = chatMsg.getToUserId();
        String toGroupId = chatMsg.getToGroupId();

        if ((toUserId == null || toUserId.length() == 0) && toGroupId == null) {
            ctx.response(new ResponseContext(ResponseCode.BAD_REQUEST_PARAMETER));
            return;
        }

        if (ctx.getFromUserId().equals(toUserId)) {
            ctx.response(new ResponseContext(ResponseCode.BAD_REQUEST_PARAMETER, "不能向自己发送消息"));
            return;
        }

        EventType eventType = toGroupId == null ? EventType.chat_f2f : EventType.chat_group;

        MsgPushContext<ChatMsgPush> msgPushContext = null;
        if (eventType == EventType.chat_f2f) {

            // TODO: 可以考虑使用缓存
            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setUserId(ctx.getFromUserId());
            friendRelation.setFriendUserId(toUserId);
            List<FriendRelation> friendRelations = friendRelationMapper.selectByCondition(friendRelation);
            if (friendRelations == null || friendRelations.isEmpty()) {
                ctx.response(new ResponseContext(ResponseCode.NOT_YOUR_FRIEND));
                return;
            }

            msgPushContext = new MsgPushContext<>(
                    eventType,
                    ctx.getFromUserId(),
                    chatMsg.getToUserId(),
                    new ChatMsgPush(
                            chatMsg.getMsg()
                    )
            );

        } else {

            // TODO: 可以考虑使用缓存
            msgPushContext = new MsgPushContext<>(
                    eventType,
                    ctx.getFromUserId(),
                    chatMsg.getToUserId(),
                    new ChatMsgPush(
                            chatMsg.getToGroupId(),
                            chatMsg.getMsg()
                    )
            );
        }

        ctx.response(chatEventPush.pushChatMsg(msgPushContext));
    }
}