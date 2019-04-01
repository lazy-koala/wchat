package com.thankjava.wchat.controller;

import com.thankjava.toolkit.core.reflect.BeanCopierUtil;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.RequestContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.controller.group.GroupAdd;
import com.thankjava.wchat.bean.controller.group.GroupAddReply;
import com.thankjava.wchat.bean.notice.group.GroupAddPush;
import com.thankjava.wchat.bean.notice.group.GroupAddReplyPush;
import com.thankjava.wchat.bean.notice.group.GroupJoinPush;
import com.thankjava.wchat.bean.notice.group.GroupUser;
import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.db.entity.GroupApply;
import com.thankjava.wchat.db.entity.GroupInfo;
import com.thankjava.wchat.db.entity.GroupRelation;
import com.thankjava.wchat.db.mongo.GroupApplyMapper;
import com.thankjava.wchat.db.mongo.GroupInfoMapper;
import com.thankjava.wchat.db.mongo.GroupRelationMapper;
import com.thankjava.wchat.db.mongo.UserMapper;
import com.thankjava.wchat.db.mongo.impl.GroupApplyMapperImpl;
import com.thankjava.wchat.db.mongo.impl.GroupInfoMapperImpl;
import com.thankjava.wchat.db.mongo.impl.GroupRelationMapperImpl;
import com.thankjava.wchat.db.mongo.impl.UserMapperImpl;
import com.thankjava.wchat.notice.GroupEventPush;
import com.thankjava.wchat.ws.anno.WSController;
import com.thankjava.wchat.ws.anno.WSProcess;

import java.util.ArrayList;
import java.util.List;

@WSController(path = "group")
public class Group {

    private GroupInfoMapper groupInfoMapper = new GroupInfoMapperImpl();
    private GroupRelationMapper groupRelationMapper = new GroupRelationMapperImpl();
    private GroupApplyMapper groupApplyMapper = new GroupApplyMapperImpl();
    private GroupEventPush groupEventPush = new GroupEventPush();
    private UserMapper userMapper = new UserMapperImpl();

    @WSProcess(path = "add")
    public void add(RequestContext ctx) {

        GroupAdd groupAdd = ctx.getData(GroupAdd.class);
        // 验证群组存在性
        GroupInfo groupInfo = groupInfoMapper.selectById(groupAdd.getGroupId());
        if (groupInfo == null) {
            ctx.response(new ResponseContext(ResponseCode.GROUP_NOT_EXIST));
            return;
        }

        // 验证是否发起过申请
        GroupApply groupApply = new GroupApply();
        groupApply.setProcessed(false);
        groupApply.setToGroupId(groupAdd.getGroupId());
        groupApply.setApplyUserId(ctx.getFromUserId());
        List<GroupApply> groupApplies = groupApplyMapper.selectByCondition(groupApply);
        if (groupApplies != null && !groupApplies.isEmpty()) {
            ctx.response(new ResponseContext(ResponseCode.EXIST_GROUP_APPLY));
            return;
        }

        // 验证是否已经加入群组
        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setGroupId(groupAdd.getGroupId());
        groupRelation.setUserId(ctx.getFromUserId());
        List<GroupRelation> groupRelations = groupRelationMapper.selectByCondition(groupRelation);
        if (groupRelations != null && !groupRelations.isEmpty()) {
            ctx.response(new ResponseContext(ResponseCode.ALREADY_GROUP_ELATION));
            return;
        }

        groupApply.setRemark(groupAdd.getRemark());

        String groupApplyId = groupApplyMapper.insert(groupApply);
        GroupAddPush groupAddPush = BeanCopierUtil.copy(groupInfo, GroupAddPush.class);
        BeanCopierUtil.append(ctx.getAuthUser(), groupAddPush);
        groupAddPush.setGroupHeadImg(groupInfo.getHeadImg());
        groupAddPush.setRemark(groupAdd.getRemark());
        groupAddPush.setGroupApplyId(groupApplyId);
        groupAddPush.setGroupId(groupInfo.getId());
        groupAddPush.setUserId(ctx.getFromUserId());

        // 向群主推送申请消息
        ResponseContext responseContext = groupEventPush.pushGroupAdd(new MsgPushContext<>(
                EventType.group_add,
                ctx.getFromUserId(),
                groupInfo.getOwnerUserId(),
                groupAddPush
        ));

        ctx.response(responseContext);
    }

    @WSProcess(path = "add_reply")
    public void addReply(RequestContext ctx) {

        GroupAddReply groupAddReply = ctx.getData(GroupAddReply.class);
        if (groupAddReply.getGroupApplyId() == null || groupAddReply.getGroupApplyId().length() == 0) {
            ctx.response(new ResponseContext(ResponseCode.BAD_REQUEST_PARAMETER));
            return;
        }

        GroupApply groupApply = groupApplyMapper.selectById(groupAddReply.getGroupApplyId());

        if (groupApply == null) {
            ctx.response(new ResponseContext(ResponseCode.GROUP_APPLY_NOT_EXIST));
            return;
        }

        if (groupApply.getProcessed()) {
            ctx.response(new ResponseContext(ResponseCode.GROUP_APPLY_PROCESSED));
            return;
        }

        GroupInfo groupInfo = groupInfoMapper.selectById(groupApply.getToGroupId());

        if (groupAddReply.getAgree()) {
            groupApply.setAgree(true);
            groupApply.setProcessed(true);
        } else {
            // 已拒绝
            groupApply.setAgree(false);
            groupApply.setProcessed(true);
        }

        groupApplyMapper.updateById(groupApply);

        GroupAddReplyPush groupAddReplyPush = new GroupAddReplyPush();
        groupAddReplyPush.setAgree(groupApply.getAgree());
        groupAddReplyPush.setGroupHeadImg(groupInfo.getHeadImg());
        groupAddReplyPush.setGroupId(groupInfo.getId());
        groupAddReplyPush.setGroupNickname(groupInfo.getGroupNickname());
        groupAddReplyPush.setIntroduction(groupInfo.getIntroduction());
        groupAddReplyPush.setOwnerUserId(groupInfo.getOwnerUserId());
        groupAddReplyPush.setGroupName(groupInfo.getGroupName());

        GroupJoinPush groupJoinPush = BeanCopierUtil.copy(groupAddReplyPush, GroupJoinPush.class);

        BeanCopierUtil.append(userMapper.selectById(groupApply.getApplyUserId()), groupJoinPush);
        groupJoinPush.setUserId(groupApply.getApplyUserId());

        GroupRelation groupRelation = new GroupRelation();
        groupRelation.setUserId(groupApply.getApplyUserId());
        groupRelation.setGroupId(groupInfo.getId());

        groupRelationMapper.insert(groupRelation);

        List<GroupUser> groupUsers = new ArrayList<>();
        List<GroupRelation> groupRelations = groupRelationMapper.selectByGroupId(groupInfo.getId());
        for (GroupRelation g : groupRelations) {
            groupUsers.add(BeanCopierUtil.copy(userMapper.selectById(g.getUserId()), GroupUser.class));
        }

        groupAddReplyPush.setGroupUsers(groupUsers);

        // 向申请人推送结果
        groupEventPush.pushGroupAddReply(new MsgPushContext<>(
                EventType.group_add_result,
                ctx.getFromUserId(),
                groupApply.getApplyUserId(),
                groupAddReplyPush
        ));


        if (groupAddReply.getAgree()) {
            // 向成员推送新用户入群通知
            groupEventPush.pushGroupUserJoin(new MsgPushContext<>(
                    EventType.group_join,
                    ctx.getFromUserId(),
                    null,
                    groupJoinPush
            ));
        }

        ctx.response(new ResponseContext(ResponseCode.SUCCESS));
    }
}
