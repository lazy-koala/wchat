<template>
    <div class="list">
    <el-tabs :stretch = "true" class="el-tabs"  @tab-click="changeType">
        <el-tab-pane label="好友">
            <ul>
                <li @click="selectSession(item, false)" class="list-li" v-for="item in friendList" :class="{ active: item.userId === currentFriend.friendId }">
                    <info-card :userId="item.userId" :isSelf="isSelf"></info-card>
                    <p class="name">{{item | nameText}}<span class="status el-icon-circle-check" :class="statusChange(item)"></span></p>
                </li>
            </ul>
        </el-tab-pane>
        <el-tab-pane label="群组">
            <ul class="list-wrapper">
                <li class="list-li" v-for="item in groupList" :class="{ active: item.groupId === currentGroup.groupId }" @click="selectSession(item, true)">
                    <group-info-card :userId="item.groupId" :groupInfo="item" :isSelf="isSelf"></group-info-card>
                    <p class="name">{{item.groupNickname || item.groupName}}</p>
                </li>
            </ul>
        </el-tab-pane>
    </el-tabs>

    </div>
</template>
<script type="text/javascript">
import InfoCard from '../common/InfoCard';
import GroupInfoCard from '../common/GroupInfoCard';
import Cookies from "js-cookie";
import Common from "../../assets/scripts/common.js";
import { mapActions, mapGetters } from "vuex";

export default {
    name: 'List',
    components: {
        'info-card': InfoCard,
        'group-info-card': GroupInfoCard
    },
    data() {
        return {
            isSelf: false,
            isGroup: false
        }
    },
    computed: {
        ...mapGetters([
            'friendList',
            'currentFriend',
            'groupList',
            'currentGroup',
            'hasGroupFriendList'
        ])
    },
    filters: {
        nameText: function (user) {
            let name = '';
            let status = {
                '0': '(离线)',
                '1': '(在线)'
            };
            user.nickname ? (name = user.nickname) : (name = user.username);
            return name;
        }
    },
    methods: {
        ...mapActions([
            'initGroupFriendList'
        ]),
        selectSession: function (item, isGroup) {
            if(isGroup) {
                var group = {
                    id: item.groupId,
                    name: item.groupNickname || item.groupName
                };

                // 跟新当前所选群组
                this.updateCurrentGroup(group);

                // 获取当前所选群组的好友列表
                this.getGroupUserList(this.currentGroup.groupId);

            } else {
                // 更新当前所选好友
                var friend = {
                    id: item.userId,
                    name: item.nickname || item.username
                };
                this.updateCurrentFriend(friend);
            }

        },

        statusChange: function (user) {
            return user.status == '1' ? '' : 'offline';
        },

        changeType: function (type) {
            if (type.index == '1') {
                this.isGroup = true;
            }
            this.updateType(type.index);
        },

        getGroupUserList: function (groupId) {
            var that  = this;
            let hasGroupFriend = that.$store.getters.hasGroupFriendList(groupId);
            if(hasGroupFriend) {
                return false;
            }
            Common.axios({
                url: 'getGroupFriendList',
                data: {
                    groupId: groupId
                }
            }).then((res) => {
                if (res.data) {
                    let friendList = [];
                    that.initGroupFriendList({
                        list: res.data[groupId],
                        groupId: groupId
                    });
                }
            }, (error) => {

            });
        },


        ...mapActions([
            'updateFriendList',
            'updateCurrentFriend',
            'updateType',
            'updateGroupList',
            'updateCurrentGroup',
            'updateSessions',
        ])
    },
    created() {
        let that = this;
        let token = Cookies.get('token');

        Common.axios({
            url: 'getFriendList'
        }).then((res) => {
            let userList = res.data.list || [];
            let user = userList[0] || {};
            // 初始化当前选择用户
            this.updateCurrentFriend({
                id: user.userId,
                name: user.nickname || user.username
            });
            // 更新朋友列表
            this.updateFriendList({
                list: userList,
                type: 0
            });

            // 初始化好友会话session
            this.updateSessions({
                id: user.userId,
                session: {}
            });

        }, (error) => {

        });

        // 获取群组列表
        Common.axios({
            url: 'getGroupList',
            data: {type: '1'}
        }).then((res) => {
            if (res && res.data && res.data.list) {
                let groupList = res.data.list || [];
                let group = groupList[0] || {};
                if (groupList.length > 0) {
                    // 更新群列表
                    that.updateGroupList({
                        list: groupList,
                        type: 0
                    });
                    // 默认当前选中群组为第一个
                    that.updateCurrentGroup({
                        id: group.groupId,
                        name: group.groupNickname || group.groupName
                    });

                    // 获取当前所选群组的好友列表
                    that.getGroupUserList(group.groupId);

                }
            }
        });

    }
}
</script>
<style type="text/css" lang="scss">
.el-tabs__nav .el-tabs__item {
  color: #eee;
}
.el-select-dropdown__item {
    border-bottom: none;
}
.list-wrapper {
    width: 2.2rem;
    max-height: 4.5rem;
    overflow-y: scroll;
}
.list-wrapper::-webkit-scrollbar {
    display: none;
}
.list-li {
    display: flex;
    align-items: center;
    height: 0.5rem;
    border-bottom: 1px solid #292C33;
    cursor: pointer;
    transition: background-color .1s;
    padding-left: 0.1rem;

    &:hover {
        background-color: rgba(255, 255, 255, 0.03);
    }
    &.active {
        background-color: rgba(255, 255, 255, 0.1);
    }
}

.avatar {
    width: 0.3rem;
    height: 0.3rem;
    display: inline-block;
    border-radius: 0.02rem;
}
.name {
    display: inline-block;
    margin: 0 0 0 0.15rem;
    padding-right: 0.05rem;
}
.status {
    padding-left: 0.05rem;
    color: #09BB07;
}
.offline {
    color: #53544F;
    font-size: 0;
}
</style>