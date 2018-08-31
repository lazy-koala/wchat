![license](https://img.shields.io/badge/license-GNU-100000.svg)
![vue](https://img.shields.io/badge/-vue-lightred.svg)
![node](https://img.shields.io/badge/-node-green.svg)
![koa2](https://img.shields.io/badge/-koa2-blue.svg)
![java](https://img.shields.io/badge/-java-green.svg)
![websocket](https://img.shields.io/badge/-websocket-lightred.svg)
---
> ### 基于WebScoket的即时IM聊天工具 
- Why

  ```
  QQ微信或者其他沟通工具不能使用？Try this 与外界获得联系。😄
  ```
> ### 在线使用 [wchat](https://wchat.thankjava.com)

- 技术栈

  ```
  Browser <---------> WebSocket
    |	                 |
    |	                 |
    V                      V
   HTTP   -----------> MongoDB

  >
  > vue(UI)、node(http srv)、java(websocket srv)
  >
  ```
- 接口文档
  - [http接口文档](https://github.com/lazy-koala/wchat/blob/master/doc/api/http.md)
  - [websocket接口文档](https://github.com/lazy-koala/wchat/blob/master/doc/api/websocket.md)
  
- ChangeLog
  ```
   - 1.0.0 初始版本
      开发中，预计10月初上线
  ```
> #### 关于我们

[![org](https://img.shields.io/badge/org-@LazyKoala-yellow.svg)](https://github.com/lazy-koala/)

[![author](https://img.shields.io/badge/author-@qazyuan-blue.svg)](https://github.com/qazyuan/) [![author](https://img.shields.io/badge/author-@thankjava-blue.svg)](https://github.com/thankjava/)

---
> #### 备注
- 由于服务端不记录任何对话内容（包括日志，数据记录等均不追踪用户聊天内容），所以暂无历史记录
