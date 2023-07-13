package com.thankjava.wchat.notice;

import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.notice.notice.OfflinePush;
import com.thankjava.wchat.bean.notice.notice.OnlinePush;
import com.thankjava.wchat.db.entity.FriendRelation;
import com.thankjava.wchat.db.mongo.FriendRelationMapper;
import com.thankjava.wchat.db.mongo.impl.FriendRelationMapperImpl;
import com.thankjava.wchat.util.WSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StatusChangeEventPush {

    private final FriendRelationMapper friendRelationMapper = new FriendRelationMapperImpl();
    static Logger logger = LoggerFactory.getLogger(StatusChangeEventPush.class);

    public void pushOnline(MsgPushContext<OnlinePush> msgPushContext) {
        doPush(msgPushContext);
    }

    public void pushOffline(MsgPushContext<OfflinePush> msgPushContext) {
        doPush(msgPushContext);
    }

    public void pushForcedLogout(MsgPushContext msgPushContext) {
        try {
            WSUtil.sendMsg(msgPushContext);
            WSUtil.closeConn(msgPushContext.getToUserId().concat(";/notice/event"));
        } catch (Exception e) {
            // ignore
        }

    }

    private void doPush(MsgPushContext msgPushContext) {

        FriendRelation query = new FriendRelation();

        query.setUserId(msgPushContext.getFromUserId());

        List<FriendRelation> friendRelations = friendRelationMapper.selectByCondition(query);

        if (friendRelations == null || friendRelations.isEmpty()) {
            return;
        }

        for (FriendRelation friendRelation : friendRelations) {
            
            WSUtil.sendMsgAsync(new MsgPushContext<>(
                    msgPushContext.getEventType(),
                    msgPushContext.getFromUserId(),
                    friendRelation.getFriendUserId(),
                    msgPushContext.getData()
            ));
        }

    }

}
