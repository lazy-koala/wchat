/**
 * 与服务端建立获取事件通知的websocket连接
 * @author acexy@thankjava.com
 * @param url websocket 地址   ws://xxx.com/notice/event
 * @param onOpen        当与服务端建立好连接后回调该函数
 * @param onError       当与服务端发生通讯异常时回调该函数
 * @param onClose       当连接被关闭回调该函数
 * @param eventCallbackFn     注册通知事件的业务逻辑函数 例如:
 *                            {
 *                                chat_f2f: function(data) {
 *
 *                                },
 *                            }
 *                            该模块将在服务端通知过来后根据通知类型调用对应的业务函数
 * @param offlineCallback 掉线回调
 */

let logout = false;
let reTryCon;
let ws;

export function wsNoticeEvent(host, onOpen, onError, onClose, eventCallbackFn, offlineCallback) {
  ws = new WebSocket(host + '/ws/notice/event');
  ws.onopen = event => {
    reTryCon = 0;
    if (onOpen) onOpen(event);
  };

  ws.onclose = event => {
    if (onClose) onClose(event);
    console.log("与服务器断开连接...");
    if (event.code == 1008) {
      console.log('鉴权失败,重新登录');
      logout = true;
      offlineCallback();
      return;
    }
    if (!logout) {
      console.log("重连服务器...");
      // 断线重连
      wsNoticeEvent(host, onOpen, onError, onClose, eventCallbackFn, offlineCallback);
      reTryCon++;
      if (reTryCon > 5) {
        logout = true;
        reTryCon = 0;
        console.log("重连失败!");
        offlineCallback();
      }
    }
  };

  ws.onerror = event => {
    console.log("与服务端连接异常: " + ws.readyState);
    if (ws.readyState == WebSocket.CLOSED || ws.readyState == WebSocket.CLOSING) {
      if (!logout) {
        console.log("重连服务器");
        // 断线重连
        wsNoticeEvent(host, onOpen, onError, onClose, eventCallbackFn, offlineCallback);
        reTryCon++;
        if (reTryCon > 5) {
          logout = true;
          reTryCon = 0;
          console.log("重连失败");
          offlineCallback();
        }
      }
    }
    if (onError) onError(event);
  };

  ws.onmessage = event => {
    let data = JSON.parse(event.data);
    //TODO: 调试输出
    console.log(event.data);
    let eventType = data.eventType;
    if (eventType == 'forced_logout') logout = true;
    let fn = eventCallbackFn[eventType];
    if (fn) {
      fn(data);
    }
  };
}
