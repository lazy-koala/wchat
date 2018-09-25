package com.thankjava.wchat.controller;

import com.thankjava.toolkit.reflect.BeanCopier;
import com.thankjava.wchat.bean.controller.friend.FriendDel;
import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.RequestContext;
import com.thankjava.wchat.bean.ResponseContext;
import com.thankjava.wchat.bean.controller.friend.FriendAdd;
import com.thankjava.wchat.bean.controller.friend.FriendAddReply;
import com.thankjava.wchat.bean.notice.friend.FriendAddPush;
import com.thankjava.wchat.bean.notice.friend.FriendAddReplyPush;
import com.thankjava.wchat.consts.ResponseCode;
import com.thankjava.wchat.db.entity.FriendApply;
import com.thankjava.wchat.db.entity.FriendRelation;
import com.thankjava.wchat.db.entity.User;
import com.thankjava.wchat.db.mongo.FriendApplyMapper;
import com.thankjava.wchat.db.mongo.FriendRelationMapper;
import com.thankjava.wchat.db.mongo.UserMapper;
import com.thankjava.wchat.db.mongo.impl.FriendApplyMapperImpl;
import com.thankjava.wchat.db.mongo.impl.FriendRelationMapperImpl;
import com.thankjava.wchat.db.mongo.impl.UserMapperImpl;
import com.thankjava.wchat.notice.FriendEventPush;
import com.thankjava.wchat.util.WSUtil;
import com.thankjava.wchat.ws.anno.WSController;
import com.thankjava.wchat.ws.anno.WSProcess;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
@WSController(path = "friend")
public class Friend {

    private UserMapper userMapper = new UserMapperImpl();
    private FriendRelationMapper friendRelationMapper = new FriendRelationMapperImpl();
    private FriendApplyMapper friendApplyMapper = new FriendApplyMapperImpl();

    private FriendEventPush friendEventPush = new FriendEventPush();

    @WSProcess(path = "add")
    public void add(RequestContext ctx) {

        FriendAdd friendAdd = ctx.getData(FriendAdd.class);

        // 用户存在性
        User user = userMapper.selectById(friendAdd.getUserId());
        if (user == null) {
            ctx.response(new ResponseContext(ResponseCode.USE_NOT_EXIST));
            return;
        }

        if (friendAdd.getUserId().equals(ctx.getFromUserId())) {
            ctx.response(new ResponseContext(ResponseCode.CAN_NOT_ADD_YOURSELF));
            return;
        }

        // 是否有已存在的申请记录
        FriendApply friendApply = new FriendApply();
        friendApply.setApplyUserId(ctx.getFromUserId()); // 申请添加人id
        friendApply.setProcessed(false);
        friendApply.setToUserId(friendAdd.getUserId());      // 请求添加的好友id

        List<FriendApply> friendApplies = friendApplyMapper.selectByCondition(friendApply);
        if (friendApplies != null && !friendApplies.isEmpty()) {
            ctx.response(new ResponseContext(ResponseCode.EXIST_FRIEND_APPLY));
            return;
        }

        // 是否已经建立好友关系 TODO: 还存在一种对方已经向你发送了好友请求的情况
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setUserId(ctx.getFromUserId());
        friendRelation.setFriendUserId(friendAdd.getUserId());
        List<FriendRelation> friendRelations = friendRelationMapper.selectByCondition(friendRelation);
        if (friendRelations != null && !friendRelations.isEmpty()) { // 已经是你的好友拉
            ctx.response(new ResponseContext(ResponseCode.ALREADY_FRIEND_ELATION));
            return;
        }


        friendApply.setRemark(friendAdd.getRemark());
        FriendAddPush friendAddPush = BeanCopier.copy(ctx.getAuthUser(), FriendAddPush.class);
        friendAddPush.setRemark(friendAdd.getRemark());
        friendApply.setApplyUserRead(true);
        String friendApplyId = friendApplyMapper.insert(friendApply);
        if (friendApplyId == null) {
            ctx.response(new ResponseContext(ResponseCode.FAILED));
            return;
        }

        friendAddPush.setFriendApplyId(friendApplyId);
        ResponseContext responseContext = friendEventPush.pushFriendAdd(new MsgPushContext<>(
                EventType.friend_add,
                ctx.getFromUserId(),
                friendAdd.getUserId(),
                friendAddPush
        ));

        if (responseContext.getCode().equals(ResponseCode.TARGET_OFFLINE.code)) {
            FriendApply friendApplyUpdate = new FriendApply();
            friendApplyUpdate.setId(friendApplyId);
            friendApplyUpdate.setApplyUserRead(false);
            friendApplyMapper.updateById(friendApplyUpdate);
            responseContext = new ResponseContext(ResponseCode.SUCCESS,"已发送离线申请");
        }

        ctx.response(responseContext);
    }

    @WSProcess(path = "add_reply")
    public void addReply(RequestContext ctx) {

        FriendAddReply friendAddReply = ctx.getData(FriendAddReply.class);
        String friendApplyId = friendAddReply.getFriendApplyId();
        if (friendApplyId == null || friendApplyId.length() == 0) {
            ctx.response(new ResponseContext(ResponseCode.BAD_REQUEST_PARAMETER));
            return;
        }

        FriendApply friendApply = new FriendApply();
        friendApply.setId(friendApplyId);
        friendApply.setProcessed(false);

        List<FriendApply> friendApplies = friendApplyMapper.selectByCondition(friendApply);

        if (friendApplies == null || friendApplies.isEmpty()) {
            // 好友申请已处理
            ctx.response(new ResponseContext(ResponseCode.FRIEND_APPLY_PROCESSED, "不存在的好友申请或已处理"));
            return;
        }

        friendApply = friendApplies.get(0);

        FriendAddReplyPush friendAddReplyPush = BeanCopier.copy(ctx.getAuthUser(), FriendAddReplyPush.class);
        friendAddReplyPush.setUserId(ctx.getFromUserId());

        if (friendAddReply.getAgree()) { // 同意添加好友


            FriendRelation friendRelation = new FriendRelation();
            friendRelation.setFriendUserId(friendApply.getToUserId());
            friendRelation.setUserId(friendApply.getApplyUserId());

            // 插入好友数据
            friendRelationMapper.insert(friendRelation);

            friendRelation.setUserId(friendApply.getToUserId());
            friendRelation.setFriendUserId(friendApply.getApplyUserId());
            friendRelationMapper.insert(friendRelation);

            friendApply.setProcessed(true);
            friendApply.setAgree(true);
            friendApplyMapper.updateById(friendApply);


            friendAddReplyPush.setAgree(true);

        } else {
            friendApply.setProcessed(true);
            friendApply.setAgree(false);
            friendApplyMapper.updateById(friendApply);
            friendAddReplyPush.setAgree(false);
        }

        // 向申请人推送相关数据
        friendEventPush.pushFriendAddReply(new MsgPushContext<>(
                EventType.friend_add_result,
                ctx.getFromUserId(),
                friendApply.getApplyUserId(),
                friendAddReplyPush
        ));

        ctx.response(new ResponseContext(ResponseCode.SUCCESS));
    }

    @WSProcess(path = "del")
    public void del(RequestContext ctx) {
        FriendDel friendDel = ctx.getData(FriendDel.class);
        FriendRelation friendRelation = new FriendRelation();
        friendRelation.setUserId(ctx.getFromUserId());
        friendRelation.setFriendUserId(friendDel.getUserId());
        friendRelation = friendRelationMapper.selectByCondition(friendRelation).get(0);
        friendRelationMapper.deleteById(friendRelation.getId());

        friendRelation = new FriendRelation();
        friendRelation.setUserId(friendDel.getUserId());
        friendRelation.setFriendUserId(ctx.getFromUserId());
        friendRelation = friendRelationMapper.selectByCondition(friendRelation).get(0);
        friendRelationMapper.deleteById(friendRelation.getId());

        // 向被删除的用户推送被删除信息
        ctx.response(friendEventPush.pushFriendDel(
                new MsgPushContext<>(EventType.friend_deleted, ctx.getFromUserId(), friendDel.getUserId(), null)
        ));
    }
}
