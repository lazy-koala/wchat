<template>
    <div>
        <div v-if="tabType==0">
            <p class="user-info">{{ currentFriend.friendName }}</p>
            <ul class="message" ref = 'message-box'>
                <li v-for="(item, index) in currentSession">
                    <p class="time" v-if="messageText(currentSession[index], currentSession[index-1])">
                        <span>{{ messageText(currentSession[index], currentSession[index-1]) }}</span>
                    </p>
                    <div class="main" v-if="!item.self">
                        <img class="avatar" :src="currentFriendInfo.headImg" />
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
                <p>{{ currentGroup.groupName }}</p>
                <friend-list :groupId="currentGroup.groupId"></friend-list>
            </div>
            <ul class="message" ref = 'message-box'>
                <li v-for="(item, index) in groupSession">
                    <p class="time" v-if="messageText(groupSession[index], groupSession[index-1]) && !item.isGroupJoinResult">
                        <span>{{ messageText(groupSession[index], groupSession[index-1]) }}</span>
                    </p>
                    <p class="time" v-if="item.isGroupJoinResult">
                        <span>{{ item.content }}</span>
                    </p>
                    <div class="main" v-if="!item.self && !item.isGroupJoinResult">
                        <div class="head-wrapper">
                            <p>{{groupFriendInfo.nickname || groupFriendInfo.username}}</p>
                            <img :src="groupFriendInfo.headImg" />
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
            </ul>
        </div>
    </div>
</template>
<script type="text/javascript">
import Common from "../../assets/scripts/common.js";
import FriendList from "../common/FriendList";
import $axios from 'axios';
import { mapActions, mapGetters } from "vuex";

export default {
    name: 'Messages',
    components: {
        'friend-list': FriendList
    },
    computed: {
        ...mapGetters({
            currentSession: 'currentSession',
            user: 'user',
            currentFriend:'currentFriend',
            groupSession: 'currentGroupSession',
            currentGroup: 'currentGroup',
            tabType: 'tabType'
        }),

        currentFriendInfo: function () {
            return this.$store.getters.friendInfo(this.currentFriend.friendId);
        },

        groupFriendInfo: function (userId) {
            return this.$store.getters.currentFriendList(this.currentGroup.groupId) || {};
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
        },
        ...mapActions([
            'updateGroupFriendList'
        ])
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
        currentSession: {//深度监听，可监听到对象、数组的变化
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
            this.$nextTick(function () {
                this.el.scrollTop = this.el.scrollHeight - this.el.clientHeight;
            });
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