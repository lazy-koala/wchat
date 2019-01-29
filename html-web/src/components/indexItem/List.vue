<template>
    <div class="list">
    <el-tabs :stretch = "true" class="el-tabs"  @tab-click="changeType">
        <el-tab-pane label="好友">
            <ul>
                <li class="list-li" v-for="item in friendList" :class="{ active: item.userId === currentFriendId }">
                    <info-card :userId="item.userId" :isSelf="isSelf"></info-card>
                    <p class="name">{{item | nameText}}<span class="status el-icon-circle-check" :class="statusChange(item)"></span></p>
                </li>
            </ul>
        </el-tab-pane>
        <el-tab-pane label="群组">
            <ul class="list-wrapper">
                <li class="list-li" v-for="item in groupList" :class="{ active: item.groupId === currentGroupId }" @click="selectSession(item.groupId, item.groupInfo, true)">
                    <group-info-card :userId="item.groupId" :groupInfo="item.groupInfo" :isSelf="isSelf"></group-info-card>
                    <p class="name">{{item.groupInfo.groupNickname || item.groupInfo.groupName}}</p>
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
import $axios from 'axios';

export default {
    name: 'List',
    components: {
        'info-card': InfoCard,
        'group-info-card': GroupInfoCard
    },
    data() {
        return {
            hoverId: '',
            isSelf: false,
            isGroup: false
        }
    },
    computed: {
        ...mapGetters([
            'friendList',
            'currentFriendId',
            'groupList'
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
            this.updateType(type.index);
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
        },
        ...mapActions([
            'updateFriendList',
            'updateCurrentId',
            'updateType',
            'updateGroupList',
            'updateCurrentGroupId'
        ])
    },
    created() {
        let that = this;
        let token = Cookies.get('token');

        Common.axios({
            url: 'getFriendList'
        }).then((res) => {
            let userList = res.data.list || [];
            let id = userList[0].userId || '';
            that.hoverId = id;
            this.updateCurrentId(id);
            this.updateFriendList(userList);
        }, (error) => {

        });

        // 获取群组列表
        Common.axios({
            url: 'getGroupList',
            params: {type: '1'}
        }).then((res) => {
                if (res && res.data && res.data.data) {
                    let groupList = res.data.data.list || [];
                    let groupId = groupList[i].groupId;
                    if (groupList.length > 0) {
                        // 更新群列表
                        that.updateGroupList(groupList);
                        // 默认当前选中群组为第一个
                        that.updateCurrentGroupId(groupId)
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