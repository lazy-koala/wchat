package com.thankjava.wchat.ws.core;

import com.thankjava.toolkit.core.reflect.BeanCopierUtil;
import com.thankjava.toolkit.core.thread.ThreadTask;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.notice.notice.OfflinePush;
import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.consts.RedisKeyManager;
import com.thankjava.wchat.lib.websocket.callback.OnConnCloseListener;
import com.thankjava.wchat.lib.websocket.entity.VerifiedConnection;
import com.thankjava.wchat.notice.StatusChangeEventPush;
import com.thankjava.wchat.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnConnCloseCallBack implements OnConnCloseListener {

    private StatusChangeEventPush statusChangeEventPush = new StatusChangeEventPush();

    private static Logger logger = LoggerFactory.getLogger(OnConnCloseCallBack.class);

    // 离线推送做delay时间控制
    private static ThreadTask threadTask = new ThreadTask(50);

    // 延时时间
    final int delayTime = 3;

    @Override
    public void doProcess(VerifiedConnection verifiedConnection) {
        logger.debug("connection closed: sessionId = {} , path = {}", verifiedConnection.getSessionId(), verifiedConnection.getPath());
        if ("/notice/event".equals(verifiedConnection.getPath())) {

            // 如果当前在线则执行下线 并准备推送离线信息
            if (RedisUtil.getRedisManager().sismember(RedisKeyManager.ONLINE_SET_KEY(), verifiedConnection.getUserId())) {

                RedisUtil.getRedisManager().srem(RedisKeyManager.ONLINE_SET_KEY(), verifiedConnection.getUserId());

                threadTask.addTaskRunOnce(delayTime, () -> {

                    // 延迟指定时间后 如果确实离线才推送
                    if (!RedisUtil.getRedisManager().sismember(RedisKeyManager.ONLINE_SET_KEY(), verifiedConnection.getUserId())) {

                        logger.info("push offline userId = " + verifiedConnection.getUserId());

                        MsgPushContext<OfflinePush> msgPushContext = new MsgPushContext<>(
                                EventType.friend_status_change,
                                verifiedConnection.getUserId(),
                                null,
                                BeanCopierUtil.copy(verifiedConnection.getBindData(), OfflinePush.class)
                        );
                        statusChangeEventPush.pushOffline(msgPushContext);
                    }

                });
            }
        }
    }
}
