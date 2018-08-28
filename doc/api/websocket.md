>  ### WebSocket 接口文档
---
> #### 业务请求
```
向服务端发送消息 (根据不同业务请求不同path)
```
- 请求接口清单

    地址 | 业务功能
    --- | ---
    /ws/chat/send_msg | 发送好友/群消息 (该请求为长联状态)
    /ws/friend/add | 请求添加指定用户为好友
    /ws/friend/add_reply | 处理好友申请
    /ws/friend/del | 删除好友
    /ws/group/add | 申请加入群组
    /ws/group/add_reply | 处理群组申请
    
- 请求参数格式 ( * 表示该属性必传)

    - /ws/chat/send_msg
      
        - 请求
        
            ```json
            {
            "toUserId": " 接收人userId",   
            "toGroupId": " 若消息为群消息,则上送groupId",
            "msg": "* 文本内容"
            }
            ```
        
        - 响应  
        
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```
        
    - /ws/friend/add
      
        - 请求
        
            ```json
            {
            "userId": "* 请求加好友的userId",
            "remark": " 申请备注"
            }
            ```
        
        - 响应  
        
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```
        
    - /ws/friend/add_reply
    
        - 请求
        
            ```json
            {
            "friendApplyId": "* 好友申请id",
            "agree": true
            }
            ```
        
        - 响应
        
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```
        
    - /ws/friend/del
    
        - 请求
          
            ```json
            {
            "userId": "* 需要删除的userId"
            }
            ```
        - 响应  
          
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```
        
    - /ws/group/add
    
        - 请求
          
            ```json
            {
            "groupId": "* 申请的群组id",
            "remark": "申请备注"
            }
            ```
        - 响应  
          
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```
    - /ws/group/add_reply
    
        - 请求
        
            ```json
            {
            "groupApplyId": "* 群组申请id",
            "agree": true
            }
            ```
        - 响应
        
            ```json
            {
            "code": "0000",
            "desc": "处理完成"
            }
            ```

- 响应错误码 

   错误码 | 描述 | 备注
   --- | --- | ---
   0000 | 操作完成 |
   9998 | 处理失败 |
   9997 | 请求参数错误 | 无法正确解析请求参数
   8999 | 目标已离线 | 针对发送消息类处理(暂未提供离线信息功能)
   0001 | 对方还不是你的好友 | 发送好友信息时
   0101 | 好友申请已处理 |
   0102 | 不存在的用户信息 |
   0103 | 已经是你的好友 |
   0104 | 不能添加自己 |
   0105 | 已经发送过好友申请 |
   0201 | 不存在的群组信息 |
   0202 | 已经加入该群 |
   0203 | 已经发送过入群申请 |
   0204 | 不存在的群组申请Id |
   0205 | 该群组申请已处理 |
   
---
> #### 事件通知
```
服务端推送消息 (path: /ws/notice/path)
```
- 通知事件清单

    事件类型 | 事件码 | 备注 
    --- | --- | ---
    好友消息 | chat_f2f | 好友发送消息
    群消息 | chat_group | 群组中有人发送消息
    好友申请 | friend_add | 收到请求被添加好友的推送消息
    好友删除 | friend_deleted | 被好友删除
    好友状态变化 | friend_status_change | 好友上下线
    好友申请结果 | friend_add_result | 申请添加好友通过/不通过通知
    邀请加入群组 | group_invite |
    被群组移除 | group_kicked_out |
    申请加入群组 | group_add |
    同意入群 | group_add_result | 申请入群处理通知 通过/不通过
    成员离群 | group_leave | 群成员主动离开群
    成员入群 | group_join | 新成员入群推送
    同意邀请入群 | group_invite_agree | 通过入群邀请
    拒绝入群邀请 | group_invite_refused | 
    强制登出 | forced_logout | 异地登录强制退出

- 事件通知参数格式

    - chat_f2f
      
        ```json
        {
          "data": {
            "msg": "来自好友的消息"
          },
          "eventType": "chat_f2f",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - chat_group
      
        ```json
        {
          "data": {
            "msg": "来自群的消息",
            "toGroupId": "toGroupId"
          },
          "eventType": "chat_group",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - friend_add
      
        ```json
        {
          "data": {
            "friendApplyId": "",
            "headImg": "/**/*.png",
            "nickname": "Acexy",
            "remark": "加好友~",
            "username": "acexy",
            "remark": "申请描述",
            "sex": "性别 0-女 1-男 -1-未设置" ,
            "sign": "个性签名"
          },
          "eventType": "friend_add",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - friend_add_result
      
        ```json
        {
          "data": {
            "agree": true,
            "headImg": "/**/*.png",
            "nickname": "Acexy",
            "userId": "userId",
            "username": "acexy",
            "sex": "性别 0-女 1-男 -1-未设置",
            "sign": "个性签名"
          },
          "eventType": "friend_add_result",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - friend_status_change
      
        ```json
        {
          "data": {
            "status": "1-上线 0-离线",
            "headImg": "/**/*.png",
            "nickname": "Acexy",
            "remark": "好友备注名",
            "username": "acexy"
          },
          "eventType": "friend_status_change",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - friend_deleted
      
        ```json
        {
          "eventType": "friend_deleted",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - forced_logout
    
        ```json
        {
          "eventType": "forced_logout",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - group_add
    
        ```json
        {
          "data": {
            "groupApplyId": "申请id",
            "groupId": "申请加入群id",
            "groupNickname": "群昵称",
            "groupName": "群帐号",
            "groupHeadImg": "群头像",
            "userId": "申请人uerId",
            "nickname": "用户昵称",
            "headImg": "用户头像",
            "username": "用户帐号",
            "remark": "申请备注",
            "introduction": "群简介"
          },
          "eventType": "group_add",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
        
    - group_add_result
      
        ```json
        {
          "data": {
            "agree": true,
            "groupId": "申请加入群id",
            "groupNickname": "群昵称",
            "groupName": "群帐号",
            "groupHeadImg": "群头像",
            "ownUserId": "群主userId",
            "introduction": "群简介"
          },
          "eventType": "group_add_result",
          "fromUserId": "fromUserId",
          "toUserId": "toUserId",
          "time": 1534417492018
        }
        ```
