<template>
    <div class="wrapper">
        <div class="sidebar">
            <info></info>
            <list></list>
        </div>
        <div class="main">
            <message></message>
            <text-area></text-area>
        </div>
    </div>
</template>

<script>
import Info from './indexItem/Info';
import List from './indexItem/List';
import TextArea from './indexItem/TextArea';
import Message from './indexItem/Message';
import {wsMsgSender, send2Friend, send2Group} from '../assets/scripts/ws/msgSender.js';
import {wsNoticeEvent} from "../assets/scripts/ws/noticeEvent";
import $axios from 'axios';

export default {
  name: 'Index',
  components: {Info, List, TextArea, Message},
  methods: {
    notice: function (id, isGroup) {
      var that = this;
      if (Notification.permission == "granted") {
        that.showNotice(id, isGroup);

      } else if (Notification.permission != "denied") {
          Notification.requestPermission(function (permission) {
            if (permission == 'granted') {
              that.showNotice(id, isGroup);
            }
          });
      }
    },

    showNotice: function (id, isGroup) {
      var that = this;
      var name = '';
      var info;
      var headImg;
      var state = this.$store.state;
      var sessions = state.sessions;
      var groupSessions = state.groupSessions;
      if (isGroup) {
        info = groupSessions.find(item => item.groupId === id);
        name = info ? (info.groupInfo.groupNickname || info.groupInfo.groupName) : '';
        headImg = info ? info.groupInfo.groupHeadImg : '';
      } else {
        info = sessions.find(item => item.userId === id);
        headImg = info ? info.user.headImg : '';
        name = info ? (info.user.nickname || info.user.username) : '';
      }
      var notification = new Notification("新消息提示", {
          body: name ? '哎呦，你有来自' + name + '的新消息~' : '哎呦，你有新消息~',
          icon: headImg,
          tag: new Date(),
          renotify: true
      });
      that.timer = setTimeout(function () {
        notification.close();
        setTimeout(that.timer);
      }, 5000);
      notification.onclick = function () {
        notification.close();
        setTimeout(that.timer);        
      }
    }
  },
  mounted () {
    let that = this;

    wsMsgSender('wss://wchat.thankjava.com');
    wsNoticeEvent('wss://wchat.thankjava.com', function (event) {
    }, function (event) {
    }, function (event) {
    }, {
      chat_f2f: function (data) {
        var item = {
          msg: data.data.msg,
          userId: data.fromUserId
        }
        if (window.Notification) {
          that.notice(data.fromUserId, false);
        }
        that.$store.commit('REVICE_MESSAGE', item);
      },
      chat_group: function (data) {
        var item = {
          msg: data.data.msg,
          userId: data.fromUserId,
          groupId: data.data.toGroupId
        }
        if (window.Notification) {
          that.notice(data.data.toGroupId, true);
        }
        that.$store.commit('REVICE_GROUP_MESSAGE', item);
      },
      friend_add: function (data) {
        data.addNews = true;
        that.$store.commit('UPDATE_NEWS_LIST', data);
      },
      friend_add_result: function (data) {
        let result = data.data;
        if (result.agree) {
          that.$notify({
              title: '提示',
              message: (result.nickname || result.username) + '已添加你为好友',
              type: 'success'
          });
          that.$store.commit('UPDATE_SESSIONS_LIST', result);
        } else {
          that.$notify({
              title: '提示',
              message: '喔豁' + (result.nickname || result.username) + '已拒绝添加你为好友',
              type: 'warning'
          });
        }
      },
      friend_status_change: function (data) {
        let userInfo = data.data;
        let name = userInfo.nickname || userInfo.username;
        // if (userInfo.status == 1) {
        //   that.$notify({
        //       title: '提示',
        //       message: '你的好友' + name + '上线啦',
        //       type: 'success'
        //   });
        // }
        that.$store.commit('UPDATE_STATUS', data);
      },
      group_add: function (data) {
        data.addNews = true;
        that.$store.commit('UPDATE_GROUP_NEWS_LIST', data);
      },
      group_add_result: function (data) {
        let result = data.data;
        let userId = that.$store.state.user.id;
        let groupId = result.groupId;
        if (result.agree && userId != result.ownerUserId) {
          that.$notify({
              title: '提示',
              message: '你已加入' + result.groupNickname || result.groupName,
              type: 'success'
          });
          // 更新申请人的群组列表
          that.$store.commit('UPDATE_GROUPSESSIONS_LIST', result);

          // 更新新群组成员数据
          $axios.get('/api/group/get_group_user_list', {params:{"groupId": groupId}})
            .then((res) => {
                if (res && res.data && !res.data.code) {
                    var friendList = res.data.data[groupId];
                    var dataInfo = {
                      groupId: groupId,
                      agree: result.agree,
                      friendList: friendList
                    };
                    that.$store.commit('UPDATE_GROUP_FRIENDLIST', dataInfo);
                  }
            });
        } else if (!resule.agree && userId != result.ownerUserId) {
          that.$notify({
              title: '提示',
              message: '喔豁，你被拒绝加入' + result.groupNickname || result.groupName,
              type: 'warning'
          });
        }
      },
      group_join: function (data){
        // 群主同意入群后，成员加入的消息推动给所有群成员
        let result = data.data;
        data.isGroupJoinResult = true;
        if (!result.code && result.userId) {
          // 推给除了管理员和申请人之外的所有人
          // let userId = that.$store.state.user.id;

            // that.$notify({
            //   title: '提示',
            //   message: (result.nickname || result.userInfo) +  '加入' + (result.groupNickname || result.groupName),
            //   type: 'success'
            // });
          // 更新新群组成员数据
          that.$store.commit('UPDATE_GROUP_FRIENDLIST', data);

            // 需要在消息列表里面提示有人加入群聊，更新在ADD_GROUP_MESSAGE里面，并用isGroupJoinResult做区分
          that.$store.commit('ADD_GROUP_MESSAGE', data);


        }
      }
    },function () {
      // 与服务器断开连接且无法重连时调用
      // alert('喔豁掉线了');
      that.$notify({
            title: '提示',
            message: '喔豁掉线了',
            type: 'warning'
        });
    });

  }
}
</script>
<!-- <script type="text/javascript" src="../assets/scripts/ws.js"></script> -->

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss">
.wrapper {
    margin: 0.5rem auto;
    width: 8rem;
    height: 6rem;

    overflow: hidden;
    border-radius: 0.03rem;

    .sidebar, .main {
        height: 100%;
    }
    .sidebar {
        float: left;
        width: 2rem;
        color: #f4f4f4;
        background-color: #2e3238;
    }
    .main {
        position: relative;
        overflow: hidden;
        background-color: #eee;
    }
}
.el-dialog__body {
      padding: 0.1rem 0.2rem;
  }
</style>
