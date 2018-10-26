package com.thankjava.wchat.ws.core;

import com.thankjava.toolkit.core.reflect.BeanCopierUtil;
import com.thankjava.toolkit.core.thread.ThreadTask;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.notice.notice.OfflinePush;
import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.consts.RedisKeyManager;
import com.thankjava.wchat.lib.websocket.callback.OnConnCloseListener;
import com.thankjava.wchat.lib.websocket.entity.ConVerifyResult;
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
    public void doProcess(ConVerifyResult conVerifyResult) {
        if ("/notice/event".equals(conVerifyResult.getPath())) {

            // 如果当前在线则执行下线 并准备推送离线信息
            if (RedisUtil.getRedisManager().sismember(RedisKeyManager.ONLINE_SET_KEY(), conVerifyResult.getUserId())) {

                RedisUtil.getRedisManager().srem(RedisKeyManager.ONLINE_SET_KEY(), conVerifyResult.getUserId());

                threadTask.addTaskRunOnce(delayTime, () -> {

                    // 延迟指定时间后 如果确实离线才推送
                    if (!RedisUtil.getRedisManager().sismember(RedisKeyManager.ONLINE_SET_KEY(), conVerifyResult.getUserId())) {

                        logger.info("push offline userId = " + conVerifyResult.getUserId());

                        MsgPushContext<OfflinePush> msgPushContext = new MsgPushContext<>(
                                EventType.friend_status_change,
                                conVerifyResult.getUserId(),
                                null,
                                BeanCopierUtil.copy(conVerifyResult.getBindData(), OfflinePush.class)
                        );

                        statusChangeEventPush.pushOffline(msgPushContext);
                    }

                });
            }
        }
    }
}
