package com.thankjava.wchat.notice;

import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.notice.group.GroupAddPush;
import com.thankjava.wchat.bean.notice.group.GroupAddReplyPush;
import com.thankjava.wchat.bean.notice.group.GroupJoinPush;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.db.entity.GroupRelation;
import com.thankjava.wchat.db.mongo.GroupRelationMapper;
import com.thankjava.wchat.db.mongo.impl.GroupRelationMapperImpl;
import com.thankjava.wchat.util.WSUtil;

import java.util.List;

public class GroupEventPush {

    private GroupRelationMapper groupRelationMapper = new GroupRelationMapperImpl();

    public ResponseContext pushGroupAdd(MsgPushContext<GroupAddPush> msgPushContext) {
        ResponseCode responseCode = WSUtil.sendMsg(msgPushContext);
        if (responseCode == ResponseCode.TARGET_OFFLINE) {
            return new ResponseContext(ResponseCode.SUCCESS);
        }
        return new ResponseContext(responseCode);
    }

    public void pushGroupAddReply(MsgPushContext<GroupAddReplyPush> msgPushContext) {
        WSUtil.sendMsgAsync(msgPushContext);
    }

    public void pushGroupUserJoin(MsgPushContext<GroupJoinPush> msgPushContext) {
        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setGroupId(msgPushContext.getData().getGroupId());
        List<GroupRelation> groupRelations = groupRelationMapper.selectByCondition(groupRelation);
        if (groupRelations != null & !groupRelations.isEmpty()) {
            for (GroupRelation relation : groupRelations) {
                if (relation.getUserId().equals(msgPushContext.getData().getUserId())) {
                    continue;
                }
                WSUtil.sendMsgAsync(new MsgPushContext<>(
                        msgPushContext.getEventType(),
                        msgPushContext.getFromUserId(),
                        relation.getUserId(),
                        msgPushContext.getData()
                ));
            }
        }
    }
}
