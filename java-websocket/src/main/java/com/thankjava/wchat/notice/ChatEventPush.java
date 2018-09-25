package com.thankjava.wchat.notice;

import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.notice.chat.ChatMsgPush;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.db.entity.GroupRelation;
import com.thankjava.wchat.db.mongo.GroupRelationMapper;
import com.thankjava.wchat.db.mongo.impl.GroupRelationMapperImpl;
import com.thankjava.wchat.util.WSUtil;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description: 消息推送
 **/
public class ChatEventPush {

    private GroupRelationMapper groupRelationMapper = new GroupRelationMapperImpl();

    /**
     * 推送聊天消息
     *
     * @param msgPushContext
     * @return
     */
    public ResponseContext pushChatMsg(MsgPushContext<ChatMsgPush> msgPushContext) {

        EventType eventType = msgPushContext.getEventType();
        ResponseCode responseCode = null;

        if (eventType == EventType.chat_group) {

            ChatMsgPush chatMsgPush = msgPushContext.getData();

            GroupRelation query = new GroupRelation();
            query.setGroupId(chatMsgPush.getToGroupId());

            List<GroupRelation> groupRelations = groupRelationMapper.selectByCondition(query);
            if (groupRelations == null || groupRelations.isEmpty()) {
                return new ResponseContext(ResponseCode.SUCCESS);
            }

            // 推送群组内所有成员消息
            for (GroupRelation groupRelation : groupRelations) {
                if (!groupRelation.getUserId().equals(msgPushContext.getFromUserId())) {
                    WSUtil.sendMsgSync(new MsgPushContext(
                            msgPushContext.getEventType(),
                            msgPushContext.getFromUserId(),
                            groupRelation.getUserId(),
                            msgPushContext.getData()
                    ));
                }
            }

            responseCode = ResponseCode.SUCCESS;
        } else {
            responseCode = WSUtil.sendMsg(msgPushContext);
        }
        return new ResponseContext(responseCode);
    }
}
