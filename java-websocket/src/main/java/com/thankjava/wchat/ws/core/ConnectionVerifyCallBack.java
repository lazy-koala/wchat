package com.thankjava.wchat.ws.core;

import com.thankjava.toolkit.core.reflect.BeanCopierUtil;
import com.thankjava.toolkit3d.core.fastjson.FastJson;
import com.thankjava.wchat.bean.MsgPushContext;
import com.thankjava.wchat.bean.notice.notice.OnlinePush;
import com.thankjava.wchat.consts.CookieName;
import com.thankjava.wchat.consts.EventType;
import com.thankjava.wchat.consts.RedisKeyManager;
import com.thankjava.wchat.controller.Friend;
import com.thankjava.wchat.controller.Group;
import com.thankjava.wchat.controller.Notice;
import com.thankjava.wchat.db.entity.User;
import com.thankjava.wchat.lib.websocket.entity.VerifiedConnection;
import com.thankjava.wchat.lib.websocket.callback.ConnectionVerifyListener;
import com.thankjava.wchat.notice.StatusChangeEventPush;
import com.thankjava.wchat.util.RedisUtil;
import com.thankjava.wchat.util.Utils;
import com.thankjava.wchat.util.WSUtil;
import com.thankjava.wchat.ws.anno.WSController;
import com.thankjava.wchat.ws.anno.WSProcess;
import com.thankjava.wchat.controller.Chat;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ConnectionVerifyCallBack implements ConnectionVerifyListener {

    private static final Map<String, Method> processes = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private StatusChangeEventPush statusChangeEventPush = new StatusChangeEventPush();

    static {

        Set<Class<?>> controllers = new HashSet<>();

        // 初始化controller
        controllers.add(Chat.class);
        controllers.add(Notice.class);
        controllers.add(Friend.class);
        controllers.add(Group.class);

        scanController(controllers);
    }

    static Logger logger = LoggerFactory.getLogger(ConnectionVerifyCallBack.class);


    /**
     * 扫描controller 用于将执行方法绑定到请求上下文中
     *
     * @param controllerClasses controller
     */
    private static void scanController(Set<Class<?>> controllerClasses) {
        StringBuilder stringBuilder;
        for (Class<?> clazz : controllerClasses) {
            WSController ws = clazz.getAnnotation(WSController.class);
            if (ws == null) {
                continue;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                stringBuilder = new StringBuilder();
                WSProcess wsProcess = method.getAnnotation(WSProcess.class);
                if (wsProcess == null) {
                    continue;
                }
                stringBuilder.append("/").append(ws.path()).append("/").append(wsProcess.path());
                processes.put(stringBuilder.toString(), method);
                try {
                    controllers.put(stringBuilder.toString(), clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 进行连接校验和连接数据信息绑定
     */
    @Override
    public VerifiedConnection<?> doProcess(ClientHandshake handshake) {

        String path = handshake.getResourceDescriptor();
        Method process = processes.get(path);
        VerifiedConnection<?> verifiedConnection = new VerifiedConnection();
        if (process == null) {
            logger.debug("无效的ws请求地址 path = " + path);
            return verifiedConnection;
        }

        // 没有cookie信息
        if (!handshake.hasFieldValue("Cookie")) {
            logger.debug("缺失Cookie认证信息 path = " + path);
            return verifiedConnection;
        }

        String token = Utils.getValueForCookieStr(handshake.getFieldValue("Cookie"), CookieName.TOKEN_KEY);
        String uid = Utils.getValueForCookieStr(handshake.getFieldValue("Cookie"), CookieName.UID_KEY);

        if (token == null || uid == null) {
            logger.debug("缺失 uid/token 认证信息 path = " + path);
            return verifiedConnection;
        }
        String authJson = RedisUtil.getRedisManager().get(RedisKeyManager.TOKEN_KEY(token));
        if (authJson == null) {
            logger.debug("无效的token = {} path = " + path, token);
            return verifiedConnection;
        }

        User user = FastJson.toObject(authJson, User.class);
        if (!user.getId().equals(uid)) {
            logger.debug("认证信息失败 uid = {} path = " + path, uid);
            return verifiedConnection;
        }

        if ("/notice/event".equals(path)) {
            if (WSUtil.isOnline(user.getId())) {

                // 当前已经在线则向目标推送强制退出
                MsgPushContext<?> msgPushContext = new MsgPushContext(EventType.forced_logout, "system", user.getId());
                statusChangeEventPush.pushForcedLogout(msgPushContext);
            } else {

                // 向好友推送上线通知
                OnlinePush onlinePush = BeanCopierUtil.copy(user, OnlinePush.class);
                statusChangeEventPush.pushOnline(new MsgPushContext<>(
                        EventType.friend_status_change,
                        uid,
                        null,
                        onlinePush
                ));

            }
        }

        RedisUtil.getRedisManager().sadd(RedisKeyManager.ONLINE_SET_KEY(), uid);
        verifiedConnection = new VerifiedConnection(uid, path, user, process, controllers.get(path));
        return verifiedConnection;
    }

}
