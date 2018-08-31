
 <h1 class="curproject-name"> wchat-http </h1> 
 wchat http api接口文档


# 公共分类

## 登录接口
<a id=登录接口> </a>
### 基本信息

**Path：** /api/login

**Method：** POST

**接口描述：**
<ul>
<li>登录成功后将返回2个cookie登录凭证信息</li>
</ul>
<pre><code>登录成功后返回cookie
token , uid

</code></pre>


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Body**

```javascript
{
  "username": "",       // * 账号
  "password": "",       // * 密码
}
```
### 返回数据

```javascript
{
    "code": "",             //   错误码 1000-账号不存在 1001-密码错误
    "message": "请求完成"   // * 结果描述
}
```
## 用户注册
<a id=用户注册> </a>
### 基本信息

**Path：** /api/registe

**Method：** POST

**接口描述：**
<p>- 注册成功可自动登录</p>


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Body**

```javascript
{
    "username": "",  // * 帐号
    "password": "",  // * 密码
    "nickname": "",  // * 昵称
    "sex": ""        // 性别 -1 0 1
}
```
### 返回数据

```javascript
{
    "code": "",     // 1005-用户名已存在;1009-注册数据异常
    "message": "",  // * 结果描述
}
```
# 个人中心

## 信息修改
<a id=信息修改> </a>
### 基本信息

**Path：** /api/user/update_uinfo

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  multipart/form-data | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| headImage | file  |  否 |    |  如果修改头像则上送 |
| nickename | text  |  否 |  张三  |  如果修改昵称则上送 |
| token | text  |  否 |    |  如果修改邮箱则上送发送验证码的凭证 |
| mail | text  |  否 |    |  如果修改邮箱则绑定上送 |
| mailCode | text  |  否 |    |  如果修改邮箱则绑定上送 |



### 返回数据

```javascript
{
    "code": "",                  // 1007-验证码过期或未获取;1008-验证码错误;1009-邮箱验证数据异常
    "message": "",               // *
    "data": {                    // *
        "id":"",                 // *
        "username": "",          // * 账号
        "nickname": "",          // * 昵称
        "email": "",             // * 电子邮箱（脱敏）
        "headImg": ""            // * 完整头像路径
    }
}
```
## 获取信息
<a id=获取信息> </a>
### 基本信息

**Path：** /api/user/get_info

**Method：** GET

**接口描述：**


### 请求参数

### 返回数据

```javascript
{
    "message": "",               // *
    "data": {                    // *
        "id":"",                 // *
        "username": "",          // * 账号
        "nickname": "",          // * 昵称
        "headImg": "",           // * 完整头像路径
        "sign": "个性签名",      // 
        "sex": "1"               // * -1 未设置 0 女 1 男
    }
}
```
## 获取待处理的好友申请
<a id=获取待处理的好友申请> </a>
### 基本信息

**Path：** /api/user/friend_apply_list

**Method：** GET

**接口描述：**


### 请求参数

### 返回数据

```javascript
{
    "message": "",              // *
    "data": {
        "list": [{
            "friendApplyId": "",        // *
            "fromUserId": "",           // * 申请人userId
            "remark": "",               // * 申请备注
            "username": "",             // *
            "nickname": "",             // *
            "headImg": "",              // *
            "sex":"",                   // *
            "sign": "",                 // *
            "createTime": 1534417492018 // *
        }]
    }
}
```
## 获取待处理的群组申请
<a id=获取待处理的群组申请> </a>
### 基本信息

**Path：** /api/user/group_apply_list

**Method：** GET

**接口描述：**


### 请求参数

### 返回数据

```javascript
{
    "message": "",              // *
    "data": {
        "list": [{
            "groupApplyId":"",           // *
            "fromUserId": "",           // * 申请人userId
            "remark": "",               // * 申请备注
            "groupName": "",             // *
            "groupNickname": "",             // *
            "groupHeadImg": "",              // *
            "username":"",
            "nickname": "",
            "headImg": "",
            "createTime": 1534417492018 // *
        }]
    }
}
```
## 密码修改
<a id=密码修改> </a>
### 基本信息

**Path：** /api/user/change_pwd

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Body**

```javascript
{
    "oldPassword": "",   // * 当前密码
    "newPassword": "",   // * 新密码
    "verifyPassword": "" // * 确认密码
}
```
### 返回数据

```javascript
{
    "code": "",           // 1001-原密码错误
    "message": "请求完成" // * 请求结果描述
}
```
# 好友管理

## 搜索用户
<a id=搜索用户> </a>
### 基本信息

**Path：** /api/friend/search_user

**Method：** GET

**接口描述：**


### 请求参数
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| nickname | 否  |  昵称 |  模糊检索   username&nickname不能同时为空 |
| username | 否  |  帐号 |  精确匹配   username&nickname不能同时为空 |

### 返回数据

```javascript
{
   "message": "操作完成",
   "data": {
      "list": [
         {
            "userId": "111",
            "username": "111",
            "nickname": "111",
            "headImg": "",
            "sex": "0",
            "sign": "lalla"
         },
         {
            "userId": "222",
            "username": "222",
            "nickname": "222",
            "headImg": "",
            "sex": "1",
            "sign": "hahha"
         }
      ]
   }
}
```
## 获取好友列表
<a id=获取好友列表> </a>
### 基本信息

**Path：** /api/friend/get_friend_list

**Method：** GET

**接口描述：**


### 请求参数

### 返回数据

```javascript
{
  "message": "操作完成",
  "data": {
    "list": [
      {
        "userId": "111",
        "username": "111",
        "nickname": "111",
        "headImg": "",
        "sex": "0",
        "sign": "lalla",
        "remark": "111",
        "status":"1",
      },
      {
        "userId": "222",
        "username": "222",
        "nickname": "222",
        "headImg": "",
        "sex": "1",
        "sign": "hahha",
        "remark": "222",
        "status":"0"
      }
    ]
  }
}
```
## 设置好友备注
<a id=设置好友备注> </a>
### 基本信息

**Path：** /api/friend/set_remark

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Body**

```javascript
{
   "friendUserId": "* 好友id",
   "remark": "备注名称"
}
```
### 返回数据

```javascript
{
    "code": "",         // 1011-对方不是你的好友
    "message": "* "
}
```
# 群组管理

## 搜索群组
<a id=搜索群组> </a>
### 基本信息

**Path：** /api/search

**Method：** GET

**接口描述：**


### 请求参数
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| groupName | 否  |  群帐号 |   |
| groupNickname | 否  |  群昵称 |   |

### 返回数据

```javascript
{
   "message": "",
   "data": {
      "list": [
         {
            "groupId": "",
            "groupName": "",
            "groupNickname": "",
            "ownerUserId": "",
            "introduction": "",
            "groupHeadImg": ""
         }
      ]
   }
}
```
## 新建群组
<a id=新建群组> </a>
### 基本信息

**Path：** /api/group/create

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Body**

```javascript
{
    "groupName":"lazy-koala",           // * 群组帐号
    "groupNickname": "技术小组",        // * 群组名称
    "introduction": "群简介",           // * 群简介
}
```
### 返回数据

```javascript
{
    "code": "",                 // 1007 - 已存在的群账号
    "message": ""               // *
}
```
## 获取群组
<a id=获取群组> </a>
### 基本信息

**Path：** /api/get_group_list

**Method：** GET

**接口描述：**


### 请求参数
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| type | 是  |  1-所有加入群   2-自己创建的群 |  群类型 |

### 返回数据

```javascript
{
  "message": "操作完成",
  "data": {
    "list": [
      {
        "ownerUserId": "",                  // 
        "ownerUername": "111",              //
        "ownnerNickname": "111",            //
        "ownerHeadImg": "",                 //
        "ownnerSex": "0",                   //
        "ownnerSign": "lalla",              //
        "remark": "111",                    //
        "groupId": "1",                     // *
        "groupName": "",                    // *
        "groupNickname": "",                // *
        "groupHeadImg": "",                 // *
        "introduction": ""                  // *
      }
    ]
  }
}
```
## 获取群组成员
<a id=获取群组成员> </a>
### 基本信息

**Path：** /api/get_group_user_list

**Method：** GET

**接口描述：**


### 请求参数
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| groupId | 是  |   |   |

### 返回数据

```javascript
{
   "message": "操作完成",
   "data": {
      "list": [
         {
            "userId": "111",
            "username": "111",
            "nickname": "111",
            "headImg": "",
            "sex": "0",
            "sign": "lalla",
            "remark": "111"
         },
         {
            "userId": "222",
            "username": "222",
            "nickname": "222",
            "headImg": "",
            "sex": "1",
            "sign": "hahha",
            "remark": "222"
         }
      ]
   }
}
```