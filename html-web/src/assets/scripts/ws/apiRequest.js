/**
 * 请求非长连的API
 * @param url
 * @param data
 * @param callback
 */
export function wsRequest(url, data, callback, failedCallback) {

  let ws = new WebSocket(url);

  ws.onopen = event => {
    if (ws && ws.readyState == WebSocket.OPEN) {
      ws.send(JSON.stringify(data));
    }
  };

  ws.onclose = event => {
    failedCallback();
  };

  ws.onerror = event => {
    failedCallback();
  };

  ws.onmessage = event => {
    // fixme 调试输出
    console.log(event.data);
    callback(JSON.parse(event.data));
    ws.close();
  }

}
