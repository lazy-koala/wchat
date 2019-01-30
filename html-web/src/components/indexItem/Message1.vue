<template>
    <div>
        <div v-if="tabType==0">
            <p class="user-info">{{ currentUser }}</p>
            <ul class="message" ref = 'message-box'>
                <li v-for="(item, index) in session.messages">
                    <p class="time" v-if="messageText(session.messages[index], session.messages[index-1])">
                        <span>{{ messageText(session.messages[index], session.messages[index-1]) }}</span>
                    </p>
                    <div class="main" v-if="!item.self">
                        <img class="avatar" :src="session.user.headImg" />
                        <div class="msg-text">{{ item.content }}</div>
                    </div>
                    <div class="main self" v-if="item.self">
                        <el-tooltip class="item" effect="dark" :content=" item.code | errorMsg" placement="top">
                            <span class="el el-icon-warn icon-warn" v-if="item.self && item.code != '0000'"></span>
                        </el-tooltip>
                        <div class="msg-text">{{ item.content }}</div>
                        <img class="avatar" :src="user.headImg" />
                    </div>
                </li>
            </ul>
        </div>
        <!-- 群组对话框 -->
        <div v-if="tabType==1">
            <div class="user-info">
                <p>{{ currentGroupName }} 111</p>
                <friend-list :groupId="groupSession.groupId"></friend-list>
            </div>
            <ul class="message" ref = 'message-box'>
                <li v-for="(item, index) in groupSession.messages">
                    <p class="time" v-if="messageText(groupSession.messages[index], groupSession.messages[index-1]) && !item.isGroupJoinResult">
                        <span>{{ messageText(groupSession.messages[index], groupSession.messages[index-1]) }}</span>
                    </p>
                    <p class="time" v-if="item.isGroupJoinResult">
                        <span>{{ item.content }}</span>
                    </p>
                    <div class="main" v-if="!item.self && !item.isGroupJoinResult">
                        <div class="head-wrapper">
                            <p>{{currentFriendList[item.userId].nickname || currentFriendList[item.userId].username}}</p>
                            <img :src="currentFriendList[item.userId].headImg" />
                        </div>
                        <div class="msg-text group">{{ item.content }}</div>
                    </div>
                    <div class="main self" v-if="item.self && !item.isGroupJoinResult">
                        <el-tooltip class="item" effect="dark" :content=" item.code | errorMsg" placement="top">
                            <span class="el el-icon-warn icon-warn" v-if="item.self && item.code != '0000'"></span>
                        </el-tooltip>
                        <div class="msg-text">{{ item.content }}</div>
                        <img class="avatar" :src="user.headImg" />
                    </div>
                </li>
                <!-- <li>
                    <div class="main">
                        <div class="head-wrapper">
                            <p>lhah</p>
                            <img src="https://source.thankjava.com/2018/7/30/992e794d7772c5cc8ecd6ca0880ab548.jpg" />
                        </div>
                        <div class="msg-text group">测试测试测试测试测试测试测试测试测试</div>
                    </div>
                </li> -->
            </ul>
        </div>
    </div>
</template>
<script type="text/javascript">
import Common from "../../assets/scripts/common.js";
import FriendList from "../common/FriendList";
import $axios from 'axios';

export default {
    name: 'News',
        data() {
            return {
                user: this.$store.state.user,
                userId: '',
                groupId: '',//当前选中的群组Id,
                friendList: {}
            }
        },
        components: {
            'friend-list': FriendList
        },
        computed: {
            session: function () {
                let state = this.$store.state;
                let sessions = state.sessions;
                let currentSessionId = state.currentSessionId;
                let temp = {};
                for (var i = 0; i < sessions.length; i++) {
                    let item = sessions[i];
                    if (item.userId == currentSessionId) {
                        temp = item;
                        this.userId = item.userId;
                    }
                }
                return temp;

            },
            currentUser: function () {
                return this.$store.state.currentUser
            },
            tabType: function () {
                return this.$store.state.tabType;
            },
            currentGroupName: function () {
                return this.$store.state.currentGroupName
            },
            groupSession: function () {
                let state = this.$store.state;
                let sessions = state.groupSessions;
                let currentGroupId = state.currentGroupId;
                let temp = {};
                let that = this;
                for (var i = 0; i < sessions.length; i++) {
                    let item = sessions[i];
                    if (item.groupId == currentGroupId) {
                        temp = item;
                        break;
                    }
                }
                return temp;
            },
            currentFriendList: function () {
              let state = this.$store.state;
              let userList = state.groupUserList[state.currentGroupId];
              let friendList = {};
              for (var i = 0; i < userList.length; i++) {
                var info = userList[i];
                friendList[info.userId] = {
                  "userId": info.userId,
                  "username": info.username,
                  "nickname": info.nickname,
                  "headImg": info.headImg,
                  "sex": info.sex,
                  "sign": info.sign,
                  "remark": info.remark
                };
              }
              return friendList;
            },
            messageText: function (item, preItem) {
              return function (item, preItem) {
                if (!preItem) {
                    return Common.formatTime(item.date);
                }
                if ((item.date - preItem.date) > 30 * 1000) {
                    return Common.formatTime(item.date);
                } else {
                    return '';
                }
              }

            },
            isShow: function () {
              var text = $(this.$refs.msgText).text();
              if (text) {
                return true;
              } else {
                return false;
              }

            }
        },
        methods: {
            dateFormat: function (time) {
                return Common.formatTime(time);
            }
        },
        filters: {
            errorMsg: function (code) {
                let msgText = {
                    '8999': '对方已离线',
                    '0001': '对方还不是你的好友',
                    '9998': '处理失败'
                }
                return msgText[code];
            }
        },
        watch: {
            session: {//深度监听，可监听到对象、数组的变化
                handler () {
                    let that = this;
                    that.$nextTick(function () {
                        let scrollDom = that.$refs['message-box'];
                        scrollDom.scrollTop = scrollDom.scrollHeight - scrollDom.clientHeight;
                    });
                },
                deep:true
            },
            groupSession: {//深度监听，可监听到对象、数组的变化
                handler () {
                    let that = this;
                    that.$nextTick(function () {
                        let scrollDom = that.$refs['message-box'];
                        scrollDom.scrollTop = scrollDom.scrollHeight - scrollDom.clientHeight;
                    });
                },
                deep:true
            }
        },
        directives: {
            // 发送消息后滚动到底部
            'scroll-bottom' () {
                // this.$nextTick(function () {
                //     this.el.scrollTop = this.el.scrollHeight - this.el.clientHeight;
                // });
            }
        }
}
</script>
<style type="text/css" lang="scss" scoped>
.message {
    height: 4rem;
    padding: 0.1rem 0.15rem;
    overflow-y: scroll;
}

.user-info {
    display: flex;
    font-size: 18px;
    height: 0.5rem;
    line-height: 0.5rem;
    padding-left: 0.15rem;
    border-bottom: 1px solid #ddd;
    p {
        flex: 1;
    }
}
li {
    margin-bottom: 0.15rem;
    overflow: hidden;
}
.time {
    // margin: 0.1rem 0;
    text-align: center;
    line-height: 0.35rem;
    > span {
        display: inline-block;
        padding: 0 0.1rem;
        font-size: 12px;
        line-height: 18px;
        color: #fff;
        border-radius: 0.02rem;
        background-color: #dcdcdc;
    }
}
.main {
    display: flex;
    align-items: center;
}
.head-wrapper {
    position: absolute;
    top: 0;
    p {
        font-size: 12px;
        color: #ccc;
        padding-bottom: 0.05rem;
    }
    img {
        border-radius: 3px;
        width: 0.3rem;
        height: 0.3rem;
    }

}
.avatar {
    position: absolute;
    top: 0;
    border-radius: 3px;
    width: 0.3rem;
    height: 0.3rem;

}
.icon-warn {
    font-size: 0.2rem;
    color: red;
}
.msg-text {
    display: inline-block;
    position: relative;
    padding: 0 0.1rem;
    margin-left: 0.4rem;
    line-height: 0.3rem;
    font-size: 14px;
    text-align: left;
    word-break: break-all;
    background-color: #fafafa;
    border-radius: 4px;

    &.group {
        margin-top: 0.2rem;
    }

    &:before {
        content: " ";
        position: absolute;
        top: 0.1rem;
        right: 100%;
        border: 0.05rem solid transparent;
        border-right-color: #fafafa;
    }
}

.self {
    float: right;
    .avatar {
        position: absolute;
        right: 0;
        top: 0;
    }
    .msg-text {
        background-color: #b2e281;
        margin-right: 0.4rem;
        margin-left: 0.02rem;
        &:before {
            right: inherit;
            left: 100%;
            border-right-color: transparent;
            border-left-color: #b2e281;
        }
    }
}
</style>