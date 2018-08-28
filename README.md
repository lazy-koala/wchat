![license](https://img.shields.io/badge/license-GNU-100000.svg)
![vue](https://img.shields.io/badge/-vue-lightred.svg)
![node](https://img.shields.io/badge/-node-green.svg)
![koa2](https://img.shields.io/badge/-koa2-blue.svg)
![java](https://img.shields.io/badge/-java-green.svg)
![websocket](https://img.shields.io/badge/-websocket-lightred.svg)
---
> ### 基于WebScoket的及时IM聊天工具 
```
Browser <---------> WebSocket
  |	                 |
  |	                 |
  V                      V
 HTTP   -----------> MongoDB
```
- vue
  ```
  UI WEB展示层
  ```

- node
  ```
  提供HTTP服务及业务处理
  ```
  
- java
  ```
  提供WebSocket服务及业务处理
  ```
