<template>
    <div class="list">
    <el-tabs :stretch = "true" class="el-tabs"  @tab-click="changeType">
        <el-tab-pane label="好友">
            <ul>
                <li class="list-li" v-for="item in sessions" :class="{ active: item.userId === currentId }" @click="selectSession(item.userId, item.user, false)">
                    <info-card :userId="item.userId" :isSelf="isSelf"></info-card>
                    <p class="name">{{item.user | nameText}}<span class="status el-icon-circle-check" :class="statusChange(item.user)"></span></p>
                </li>
            </ul>
        </el-tab-pane>
        <el-tab-pane label="群组">
            <ul class="list-wrapper">
                <li class="list-li" v-for="item in groupSessions" :class="{ active: item.groupId === currentGroupId }" @click="selectSession(item.groupId, item.groupInfo, true)">
                    <group-info-card :userId="item.groupId" :groupInfo="item.groupInfo" :isSelf="isSelf"></group-info-card>
                    <p class="name">{{item.groupInfo.groupNickname || item.groupInfo.groupName}}</p>
                </li>
            </ul>
        </el-tab-pane>
    </el-tabs>

    </div>
</template>
<script type="text/javascript">
import $axios from 'axios';
import Qs from 'qs';
import InfoCard from '../common/InfoCard';
import GroupInfoCard from '../common/GroupInfoCard';
import Cookies from "js-cookie";
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
        currentId: function () {
            return this.$store.state.currentSessionId;
        },
        currentGroupId: function () {
            return this.$store.state.currentGroupId;
        },
        sessions: function () {
            return this.$store.state.sessions;
        },
        groupSessions: function () {
            return this.$store.state.groupSessions;
        }
    },
    filters: {
        nameText: function (user) {
            let name = '';
            let status = {
                '0': '(离线)',
                '1': '(在线)'
            };
            user.remark ? (name = user.remark) : (user.nickname ? (name = user.nickname) : (name = user.username));
            return name;
        }
    },
    methods: {
        selectSession: function (id, user, isGroup) {
            let data;
            let friendList = this.$store.state.groupUserList[id] || [];

            if (isGroup && friendList.length > 0) {
                // this.$store.commit('UPDATE_CURRENT_FRIENDLIST', friendList);
                let item = {
                    id: id,
                    name: user.groupNickname || user.groupName
                }
                this.$store.commit('SELECT_GROUP_SESSION', item);
            }

            if (isGroup && friendList.length == 0) {
                $axios.get('/api/group/get_group_user_list', {params:{"groupId": id}})
                .then((res) => {
                    if (res && res.data && !res.data.code) {
                        var friendList = res.data.data[id];
                        // this.$store.commit('UPDATE_CURRENT_FRIENDLIST', friendList);
                        var item = {
                            id: id,
                            name: user.groupNickname || user.groupName
                        };
                        this.$store.commit('SELECT_GROUP_SESSION', item);

                        // that.$store.commit('UPDATE_GROUPSESSIONS_FRIENDLIST', res.data.data.list);

                    } else {
                         that.$message({
                            message: res.data.message,
                            type: 'warning'
                        });
                        return false;
                    }
                });
            }

            data = {
                id: id,
                name: (user.remark ? user.remark : (user.nickname ? user.nickname : user.username))
            };
            this.$store.commit('SELECT_SESSION', data);
        },

        statusChange: function (user) {
            return user.status == '1' ? '' : 'offline';
        },

        changeType: function (type) {
            if (type.index == '1') {
                this.isGroup = true;
            }
            this.$store.commit('CHANGE_TAB', type.index);
        },

        getGroupUserList: function (groupList) {
            let that = this;
            let groupId = [];
            for (let i = 0; i < groupList.length; i++) {
                groupId[i] = groupList[i].groupId;
            }
            var params = {
                groupId: groupId
            };
            $axios.get('/api/group/get_group_user_list', {
                params:{
                    groupId: groupId
                },
                paramsSerializer: function(params) {
                    return Qs.stringify(params, {arrayFormat: 'repeat'})
                }
            }
            ).then((res) => {
                // 将数组列表更新到state
                if (res.data.data) {
                    res.data.isInit = true;
                    that.$store.commit('UPDATE_GROUP_FRIENDLIST', res.data);
                }
            });
        }
    },
    created() {
        let that = this;
        let token = Cookies.get('token');
        // if (token) {
        //     let sessions = localStorage.getItem('chat-sessions');
        //     let data = {
        //         id: localStorage.getItem('currentSessionId'),
        //         name: localStorage.getItem('currentUser')
        //     };
        //     that.$store.commit('UPDATE_SESSIONS_FROM_LS', JSON.parse(sessions));
        //     that.$store.commit('SELECT_SESSION', data);
        // } else {

        // }

        $axios.get('/api/friend/get_friend_list', {}).then((res) => {
                if (res && res.data && res.data.data) {
                    let userList = res.data.data.list || [];
                    if (userList.length > 0) {
                        that.selectSession(userList[0].userId, userList[0], false);
                    }
                    that.$store.commit('INIT_SESSIONS', userList);
                }
        });

        // 获取群组列表
        $axios.get('/api/group/get_group_list', {params: {type: '1'}}).then((res) => {
                if (res && res.data && res.data.data) {
                    let groupList = res.data.data.list || [];
                    if (groupList.length > 0) {
                        that.selectSession(groupList[0].groupId, groupList[0], true);

                        //获取群组的群成员列表
                        that.getGroupUserList(groupList);
                        that.$store.commit('INIT_GROUP_SESSIONS', groupList);
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