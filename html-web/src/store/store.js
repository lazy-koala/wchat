import Vue from 'vue'
import vuex from 'vuex'
Vue.use(vuex);

const now = new Date();
const store = new vuex.Store({
  state: {
    // 当前用户
    user: {
      "username": "", // * 账号
      "nickname": "", // * 昵称
      "email": "", // * 电子邮箱（脱敏）
      "headImg": "",
      "sex": "-1", // * 性别 0-女 1-男 -1-未设置
      "sign": "", // * 个性签名
      "remark": "",
      'id': ''
    },
    // 好友会话列表
    sessions: [
      // {
      //     userId: '1',
      //     user: {
      //         "username": "111",          // * 账号
      //         "nickname": "111",          // * 昵称
      //         "email": "111@thankjava.com",             // * 电子邮箱（脱敏）
      //         "headImg": "./static/test.jpg",
      //         "sex": "0",                      // * 性别 0-女 1-男 -1-未设置
      //         "sign": "",                     // * 个性签名
      //         "remark": "",
      //         'id': ''
      //     },
      //     messages: [

      //     ]
      // },
    ],
    // 群组会话列表
    groupSessions: [
      // {
      //   groupId: '11',
      //   groupInfo: {


      //   },
      //   messages: [
      //     {
      //       content: '',
      //       date: new Date().getTime(),
      //       userId: ''发送消息UserId
      //       self: true,
      //       code: ''标记消息是否发送成功
      //     }
      //   ]
      // }
    ],
    // 当前选中的会话
    currentSessionId: '',
    currentUser: '',
    // 当前选中的群组
    currentGroupId: '',
    currentGroupName: '',
    currentFriendList: {},
    // 过滤出只包含这个key的会话
    filterKey: '',
    newsList: [],
    groupNewsList: [],
    tabType: '0', //0 代表好友列表 1代表群组
    groupUserList: {}
  },
  mutations: {
    // 切换群组和好友列表
    CHANGE_TAB(state, data) {
      state.tabType = data;
    },
    // 更新用户信息
    CHANGE_USERINFO(state, data) {
      for (var item in data) {
        state.user[item] = data[item];
      }
      localStorage.setItem('userInfo', JSON.stringify(state.user));
    },
    // 发送消息
    SEND_MESSAGE({ sessions, currentSessionId }, sendData) {
      let idx = -1, tempItem;
      let session = sessions.find((item, index) => {
        if (item.userId === currentSessionId) {
          idx = index;
          tempItem = item;
          return true;
        }
      });
      session.messages.push({
        content: sendData.content,
        date: new Date().getTime(),
        self: true,
        code: sendData.code
      });
      if (idx != -1) {
        sessions.splice(idx, 1);
        sessions.splice(0, 0, tempItem);
      }
    },
    // 接收消息
    REVICE_MESSAGE({ sessions, currentSessionId }, data) {
        let idx = -1, tempItem;
        let session = sessions.find((item, index) => {
          if (item.userId === data.userId) {
            idx = index;
            tempItem = item;
            return true;
          }
        });
        if (session.userId != currentSessionId) {
            session.user.isRead = false;
        };
        session.messages.push({
            content: data.msg,
            date: new Date().getTime()
        });
        if (idx != -1) {
          sessions.splice(idx, 1);
          sessions.splice(0, 0, tempItem);
        }
    },
    // 更新好友在线状态
    UPDATE_STATUS({ sessions, currentSessionId }, data) {
      // let session = sessions.find(item => item.userId === currentSessionId);
      let session = sessions.find(item => item.userId === data.fromUserId);
      session.user.status = data.data.status;
    },
    // 选择好友会话
    SELECT_SESSION(state, data) {
      state.currentSessionId = data.id;
      state.currentUser = data.name;
      let session = state.sessions.find(item => item.userId === data.id);
      if (session) {
        session.user.isRead = true;
      }
      localStorage.setItem('currentSessionId', state.currentSessionId);
      localStorage.setItem('currentUser', state.currentUser);
    },
    // 搜索好友
    SET_FILTER_KEY(state, value) {
      state.filterKey = value;
    },
    // 初始化好友对话数据
    INIT_SESSIONS(state, data) {
      for (var i = 0; i < data.length; i++) {
        let tempItem = {};
        let item = data[i];
        tempItem.userId = item.userId;
        tempItem.user = {
          "username": item.username,
          "nickname": item.nickname,
          "headImg": item.headImg,
          "sex": item.sex,
          "sign": item.sign,
          "remark": item.remark,
          'id': item.userId,
          'status': item.status,
          'isRead': true
        };
        tempItem.messages = [];
        state.sessions.push(tempItem);
        // console.log(state.sessions);
      }
    },
    // 更新好友对话列表
    UPDATE_SESSIONS_LIST(state, data) {
      let tempItem = {};
      if (data.agree) {
        tempItem.userId = data.userId;
        tempItem.user = {
          "username": data.username || '',
          "nickname": data.nickname || '',
          "headImg": data.headImg || '',
          "sex": data.sex || '',
          "sign": data.sign || '',
          "remark": data.remark || '',
          'id': data.userId,
          'status': data.status || '1',
          'isRead': true
        };
      } else {
        tempItem.userId = data.fromUserId;
        let userInfo = data.data;
        tempItem.user = {
          "username": userInfo.username || '',
          "nickname": userInfo.nickname || '',
          "headImg": userInfo.headImg || '',
          "sex": userInfo.sex || '',
          "sign": userInfo.sign || '',
          "remark": userInfo.remark || '',
          'id': data.fromUserId,
          'status': userInfo.status || '1',
          'isRead': false
        };
      }
      tempItem.messages = [];
      // state.sessions.push(tempItem);
      // 将新加好友置于顶部
      state.sessions.splice(0, 0, tempItem);
      if (state.currentSessionId == '') {
        state.currentSessionId = tempItem.userId;
        state.currentUser = tempItem.user.nickname || tempItem.user.username;
      }
    },
    // 从缓存中获取会话列表
    UPDATE_SESSIONS_FROM_LS(state, data) {
      state.sessions = Object.assign([], data);
    },
    // 更新好友消息列表
    UPDATE_NEWS_LIST (state, data) {
        if (data.addNews) {
            var userInfo = data.data;
            var item = {
               'friendApplyId': userInfo.friendApplyId,
               'headImg': userInfo.headImg,
               'nickname': userInfo.nickname,
               'remark': userInfo.remark,
               'username': userInfo.username,
               'sex': userInfo.sex,
               'sign': userInfo.sign,
               'userId': data.fromUserId
            };
            state.newsList.push(item);
        } else {
            var userId = data.userId;
            var newsList = state.newsList;
            newsList = newsList.filter(function (item) {
                return (item.userId != userId);
            });
            state.newsList = newsList;
        }
    },
    // 初始化好友请求消息列表
    INIT_NEWS_LIST (state, data) {
        for(var i = 0; i < data.length; i++) {
            var item = data[i];
            var tempItem = {
               'friendApplyId': item.friendApplyId,
               'headImg': item.headImg,
               'nickname': item.nickname,
               'remark': item.remark,
               'username': item.username,
               'sex': item.sex,
               'sign': item.sign,
               'userId': item.fromUserId
            };
            state.newsList.push(tempItem);
        }
    },
    // ************群组相关数据处理****************//
    // 初始化群组请求消息列表
    INIT_GROUP_NEWS_LIST (state, data) {
      for(var i = 0; i < data.length; i++) {
          var item = data[i];
          var tempItem = {
            "groupApplyId": item.groupApplyId,
            "fromUserId": "",
            "remark": item.remark,
            "groupName": item.groupName,
            "groupNickname": item.groupNickname,
            "groupHeadImg": item.groupHeadImg,
            "username": item.username,
            "nickname": item.nickname,
            "headImg": item.headImg,
            "createTime": 1534417492018
          };
          state.groupNewsList.push(tempItem);
      }
    },

    // 接收群组消息
    // 接收消息
    REVICE_GROUP_MESSAGE({groupSessions, currentGroupId }, data) {
        // let session = groupSessions.find(item => item.groupId === data.groupId);
        let idx = -1, tempItem;
        let session = groupSessions.find((item, index) => {
          if (item.groupId === data.groupId) {
            idx = index;
            tempItem = item;
            return true;
          }
        });
        if (session.groupId != currentGroupId) {
            session.groupInfo.isRead = false;
        };
        session.messages.push({
            content: data.msg,
            date: new Date().getTime(),
            userId: data.userId,
            self: false
        });

        if(idx != -1) {
          groupSessions.splice(idx, 1);
          groupSessions.splice(0, 0, tempItem);
        }
    },

    // 回复群消息
    SEND_GROUP_MESSAGE(state, sendData) {
      let idx = -1, tempItem;
      let groupSessions = state.groupSessions;
      let currentGroupId = state.currentGroupId;
      let session = groupSessions.find((item, index) => {
        if(item.groupId === currentGroupId) {
          idx = index;
          tempItem = item;
          return true;
        }
      });
      session.messages.push({
        content: sendData.content,
        date: new Date().getTime(),
        self: true,
        userId: state.user.id,
        code: sendData.code
      });
      if (idx != -1) {
        groupSessions.splice(idx, 1);
        groupSessions.splice(0, 0, tempItem);
      }
    },

    // group_join推送相当于在该群组添加一条消息
    ADD_GROUP_MESSAGE(state, data) {
      let sendData = data.data;
      let groupSessions = state.groupSessions;
      let currentGroupId = state.currentGroupId;
      let session = groupSessions.find(item => item.groupId === sendData.groupId);
      if (session.groupId != currentGroupId) {
          session.groupInfo.isRead = false;
      };
      session.messages.push({
        content: (sendData.nickname || sendData.username) + '加入了群聊~',
        date: new Date().getTime(),
        self: true,
        userId: state.user.id,
        isGroupJoinResult: data.isGroupJoinResult || false
      });
    },

    // 更新群组消息列表
    UPDATE_GROUP_NEWS_LIST (state, data) {
      // data.addNews为false，表示添加成功，消息列表删除一个
      if (data.addNews) {
          var groupInfo = data.data;
          var item = {
              "groupApplyId": groupInfo.groupApplyId,
              "groupId": groupInfo.groupId,
              "groupNickname": groupInfo.groupNickname || '',
              "groupName": groupInfo.groupName || '',
              "groupHeadImg": groupInfo.groupHeadImg,
              "fromUserId": groupInfo.userId,
              "toUserId": data.toUserId,
              "nickname": groupInfo.nickname || '',
              "headImg": groupInfo.headImg || '',
              "username": groupInfo.username || '',
              "remark": groupInfo.remark || '',
              "introduction": groupInfo.introduction || ''
          };
          state.groupNewsList.push(item);
      } else {
            var groupInfo = data;
            var groupId = groupInfo.groupId;
            var groupNewsList = state.groupNewsList;
            groupNewsList = groupNewsList.filter(function (item) {
                return (item.groupId != groupId);
            });
            state.groupNewsList = groupNewsList;
        }

    },

    // 初始化群组会话列表
    INIT_GROUP_SESSIONS(state, data) {
      for (var i = 0; i < data.length; i++) {
        let tempItem = {};
        let item = data[i];
        tempItem.groupId = item.groupId;
        tempItem.groupInfo = {
          "ownerUserId": item.ownerUserId,
          "ownerUername": item.ownerUername || '',
          "ownnerNickname": item.ownnerNickname || '',
          "ownerHeadImg": item.ownerHeadImg || '',
          "ownnerSex": item.ownnerSex || '-1',
          "ownnerSign": item.ownnerSign || '',
          "remark": item.remark || '',
          "groupId": item.groupId,
          "groupName": item.groupName || '',
          "groupNickname": item.groupNickname || '',
          "groupHeadImg": item.groupHeadImg || '',
          "introduction": item.introduction || '',
          "isRead": true
        };
        tempItem.messages = [];
        state.groupSessions.push(tempItem);
        // console.log(state.groupSessions);
      }
    },

    // 更新群组会话列表
    UPDATE_GROUPSESSIONS_LIST (state, data) {
      let tempItem = {};
      let userId = state.user.id;
      // 创建群组时更新会话列表
      tempItem.groupId = data.groupId || data._id;
      tempItem.groupInfo = {
        "groupId": data.groupId || data._id,
        "groupHeadImg": data.headImg || data.groupHeadImg,
        "groupName": data.groupName,           // * 群组帐号
        "groupNickname": data.groupNickname || '' ,        // * 群组名称
        "introduction": data.introduction || '',
        "ownerUserId": data.ownerUserId || userId,
        "isRead": true
      };
      tempItem.messages = [];
      // state.groupSessions.push(tempItem);

      state.groupSessions.splice(0, 0, tempItem);
      if (state.currentGroupId == '') {
        state.currentGroupId = tempItem.groupId;
        state.currentGroupName = tempItem.groupInfo.groupNickname || tempItem.groupInfo.groupName;
      }
    },

    // 更新群组群成员信息
    UPDATE_GROUP_FRIENDLIST(state, data) {
      if (data.isInit) {
        let friendData = data.data;
        for (var item in friendData) {
          state.groupUserList[item] = friendData[item];
        }
      } else if (data.isAdd) {
        state.groupUserList[data.groupId] = [];
        let userInfo = state.user;
        let ownerItem = {
          "userId": userInfo.id,
          "username": userInfo.username,
          "nickname": userInfo.nickname || '',
          "sex": userInfo.sex || -1,
          "headImg": userInfo.headImg,
          "sign": userInfo.sign
        }
        state.groupUserList[data.groupId].push(ownerItem);

      } else if(data.agree) {
        state.groupUserList[data.groupId] = [];
        for(var i=0; i < data.friendList.length; i++) {
          state.groupUserList[data.groupId].push(data.friendList[i]);
        }

      }
    },

    // 选择群组会话框
    SELECT_GROUP_SESSION(state, data) {
      state.currentGroupId = data.id;
      state.currentGroupName = data.name;
      let session = state.groupSessions.find(item => item.groupId === data.id);
      if (session && state.tabType == 1) {
        session.groupInfo.isRead = true;
      }
      localStorage.setItem('currentGroupId', state.currentGroupId);
      localStorage.setItem('currentGroupName', state.currentGroupName);
    },

    UPDATE_CURRENT_FRIENDLIST(state, data) {
      let friendList = state.currentFriendList;
      for (let i = 0; i < data.length; i++) {
        let friend = data[i];
        friendList[friend.userId] = {
          "username": friend.username,
          "nickname": friend.nickname,
          "headImg": friend.headImg,
          "sign": friend.sign,
          "ownerSign": friend.ownerSign,
          "remark": friend.remark,
          "sex": friend.sex
        }
      }
    }

  }
});

store.watch(
  (state) => {
    localStorage.setItem('chat-sessions', JSON.stringify(state.sessions));
    localStorage.setItem('news-list', JSON.stringify(state.newsList));
    return state;
  },
  (val) => {
    // console.log('CHANGE: ', val);
  }, {
    deep: true
  }
);

function changeList(sessions, id, isGroup) {
  let index = -1, item;
  for(let i = 0; i < sessions.length; i++) {
    item = sessions[i];
    if(item.userId == id) {
      index = i;
      break;
    }
  }
  if (index != -1) {
    
  }
};

export default store;
// export const actions = {
//     initData: ({ dispatch }) => store.dispatch('INIT_DATA'),
//     sendMessage: ({ dispatch }, content) => store.dispatch('SEND_MESSAGE', content),
//     selectSession: ({ dispatch }, id) => store.dispatch('SELECT_SESSION', id),
//     search: ({ dispatch }, value) => store.dispatch('SET_FILTER_KEY', value)
// };
