/**
 * @Author:acexy@thankjava.com
 * 18/8/16
 * @Description: 建立一个长期维持的用于发送消息的websocket
 */
let ws;

let path;

export function wsMsgSender(host) {
  path = host + '/ws/chat/send_msg';
  ws = new WebSocket(path);
}

const send = (toUserId, toGroupId, msg) => {
  let data = {
    toUserId: toUserId,
    msg: msg
  };
  if (toGroupId) {
    data.toGroupId = toGroupId;
  }
  return data;
};

/**
 * 发送好友消息
 * @param toUserId
 * @param toGroupId
 * @param msg
 */
export function send2Friend(toUserId, msg, callback) {
  if (ws && ws.readyState == WebSocket.OPEN) {
    ws.send(JSON.stringify(send(toUserId, null, msg)));
    ws.onmessage = event => {
      // FIXME: 调试日志
      console.log(event.data);
      callback(JSON.parse(event.data));
    }
  } else {
    // 断线重连
    ws = new WebSocket(path);
    ws.onopen = event => {
      ws.send(JSON.stringify(send(toUserId, null, msg)));
      ws.onmessage = event => {
        // FIXME: 调试日志
        console.log(event.data);
        callback(JSON.parse(event.data));
      }
    };
  }
}

/**
 * 发送群消息
 * @param toGroupId
 * @param msg
 * @param callback
 */
export function send2Group(toGroupId, msg, callback) {
  if (ws && ws.readyState == WebSocket.OPEN) {
    ws.send(JSON.stringify(send(null, toGroupId, msg)));
    ws.onmessage = event => {
      callback(JSON.parse(event.data));
    }
  } else {
    ws = new WebSocket(path);
    ws.onopen = event => {
      ws.send(JSON.stringify(send(null, toGroupId, msg)));
      ws.onmessage = event => {
        // FIXME: 调试日志
        console.log(event.data);
        callback(JSON.parse(event.data));
      }
    };
  }
}
