<template>
    <div>
        <el-dialog class="search"
          title="搜索好友或群组"
          :visible.sync="showSearchBox"
          @close='closeDialog'
          width="70%"
          :modal-append-to-body='false'
          :show-close='true'>
            <el-tabs tab-position="top">
                <el-tab-pane label="搜好友">
                    <el-input placeholder="请输入昵称或账号查找好友" v-model="name" class="input-with-select">
                    <el-select v-model="value" slot="prepend" placeholder="请选择">
                        <el-option label="昵称" value="1"></el-option>
                        <el-option label="账号" value="2"></el-option>
                    </el-select>
                        <el-button slot="append" icon="el-icon-search" @click="searchUsers">搜好友</el-button>
                    </el-input>
                    <div class="box">
                        <div class="user-item" v-for="item in userList">
                            <img :src="item.headImg">
                            <p>{{item.nickname || item.username}}</p>
                            <el-button size="mini" type="success" icon="el-icon-plus" @click="showRemark(item.userId, false)">加好友</el-button>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="搜群组">
                    <el-input placeholder="请输入群账号或者群昵称查找群组" v-model="groupname" class="input-with-select">
                    <el-select v-model="value" slot="prepend" placeholder="请选择">
                        <el-option label="群昵称" value="1"></el-option>
                        <el-option label="群账号" value="2"></el-option>
                    </el-select>
                        <el-button slot="append" icon="el-icon-search" @click="searchGroups">搜群组</el-button>
                    </el-input>
                    <div class="box">
                        <div class="user-item" v-for="item in groupList">
                            <img :src="item.groupHeadImg">
                            <p>{{item.groupNickname || item.groupName}}</p>
                            <el-button size="mini" type="success" icon="el-icon-plus" @click="showRemark(item.groupId, true)">申请入群</el-button>
                        </div>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-dialog>
        <el-dialog
          title="申请备注"
          :visible.sync="showRemarkBox"
          width="40%"
          :modal-append-to-body='false'
          :show-close='false'>
            <el-input placeholder="请输入备注" v-model="remark" class="input-with-select">
                <el-button slot="append" icon="el-icon-check" @click="confirmAdd"></el-button>
            </el-input>
        </el-dialog>
        <div class="add-user el-icon-search" title="搜索好友或群组" @click="addUser">
        </div>
    </div>
</template>
<script type="text/javascript">
import $axios from 'axios';
import {wsRequest} from "../assets/scripts/ws/apiRequest.js";
    export default {
        name: 'searchFriend',
        data () {
            return {
                showSearchBox: false,
                value: '1',
                name: '',
                groupname: '',
                userList: [],
                groupList: [],
                showRemarkBox: false,
                id: '',
                remark: '',
                isGroup: false
            }
        },
        methods: {
            addUser: function () {
                this.showSearchBox = true;
            },
            searchUsers: function () {
                var text = this.name;
                let that = this;
                let reqName = (this.value == '1' ? 'nickname' : 'username');
                let params  = {};
                params[reqName] = text;
                if (text == '') {
                    that.$notify({
                        title: '提示',
                        message: '请输入用户名或者昵称进行搜索',
                        type: 'warning'
                    });
                    return false;
                };
                $axios.get('/api/friend/search_user', {params}).then((res) => {
                    if (res && res.data && res.data.data) {
                        this.userList = res.data.data.list || [];
                    } else {
                         Message.error({
                            message: res.data.message,
                            center: true
                        });
                        return false;
                    }
                });
            },
            searchGroups: function () {
                var text = this.groupname;
                let that = this;
                let reqName = (this.value == '1' ? 'groupNickname' : 'groupName');
                let params  = {};
                params[reqName] = text;
                if (text == '') {
                    that.$notify({
                        title: '提示',
                        message: '请输入群组名称或者昵称进行搜索',
                        type: 'warning'
                    });
                    return false;
                };
                $axios.get('/api/group/search', {params}).then((res) => {
                    if (res && res.data && res.data.data) {
                        this.groupList = res.data.data.list || [];
                    } else {
                        that.$notify({
                            title: '提示',
                            message: res.data.message,
                            type: 'warning'
                        });
                        return false;
                    }
                });

            },
            showRemark: function (id, isGroup) {
                var that = this;
                this.showRemarkBox = true;
                this.id = id;
                this.isGroup = isGroup;
            },
            confirmAdd: function () {
                var that = this;
                var url = '';
                var reqData = {
                    remark: this.remark
                };
                reqData[this.isGroup ? 'groupId': 'userId'] = this.id;
                this.isGroup ? url = 'wss://wchat.thankjava.com/ws/group/add' : url = 'wss://wchat.thankjava.com/ws/friend/add'
                that.showRemarkBox = false;
                wsRequest(
                    url,
                    reqData,
                    function (data) {
                        if (data.code == '0000') {
                            that.$notify({
                                title: '提示',
                                message: '消息已发送~',
                                type: 'info'
                            });
                            that.showSearchBox = false;
                            that.name = '';
                            that.groupname = '';
                            that.remark = '';
                        } else {
                            that.$notify({
                                title: '提示',
                                message: data.desc,
                                type: 'warning'
                            });
                            that.remark = '';
                        }
                    },
                    function (data) {
                        console.log(data);
                    }
                );
            },
            closeDialog: function () {
                // 清空数据
                this.name = '';
                this.userList = [];
                this.groupList = [];
                this.groupname = '';
            }
        }
    }
</script>
<style type="text/css" lang="scss">
    .search .el-tabs__item {
      color: #303133;
    } 
    .add-user {
        line-height: 0.3rem;
        font-size: 0.15rem;
        cursor:pointer;
        
    }
    .el-select .el-input {
        width: 0.9rem;
        text-align: center;
    }
    .el-dialog__header {
        background-color: #2e3238;
    }
    .el-dialog__header .el-dialog__title {
        color: #fff;
    }
    .box {
        margin: 0.2rem auto;
        max-height: 5rem;
        min-height: 3rem;
    }
    .user-item {
        float: left;
        display: flex;
        margin-right: 0.15rem;
        margin-bottom: 0.2rem;
        vertical-align: center;
        align-items: center;
        flex-direction: column;
        img {
            width: 0.6rem;
            height:0.6rem;
            margin-bottom: 0.05rem;
        }
        p {
            margin-bottom: 0.05rem;
        }
    }
</style>
