<template>
    <div class="msg-box">
        <div class="msg-icon" @click="showBox" title="待处理消息">
            <span class="el-icon-message"></span>
            <span class="tip el el-icon-online" :class="unReadClass"></span>
        </div>
        <el-dialog
          title="消息列表"
          :visible.sync="showNewsBox"
          width="50%"
          :modal-append-to-body='false'
          :show-close='true'>
            <span class="tip el el-icon-online friend-news" :class="newsList.length ? '' : 'dn'"></span>
            <span class="tip el el-icon-online group-news" :class="groupNewsList.length ? '' : 'dn'"></span>
             <el-tabs tab-position="top">
                <el-tab-pane label="好友申请">
                    <div class="news-box">
                        <div class="news-item" v-for="item in newsList">
                            <img :src="item.headImg">
                            <div class="user-info">
                                <p>{{item.nickname || item.username}}请求添加您为好友</p>
                                <p v-if="item.remark != ''">备注： {{item.remark}}</p>
                            </div>
                            <el-button type="success" size="mini" @click="addReplay(item, true, false)">添加</el-button>
                            <el-button type="" size="mini" @click="addReplay(item, false, false)">拒绝</el-button>
                        </div>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="加群申请">
                    <div class="news-box">
                        <div class="news-item" v-for="item in groupNewsList">
                            <img :src="item.headImg">
                            <div class="user-info">
                                <p>{{item.nickname || item.username}}请求加入{{ item.groupNickname || item.groupName }}</p>
                                <p v-if="item.remark != ''">备注： {{item.remark}}</p>
                            </div>
                            <el-button type="success" size="mini" @click="addReplay(item, true, true)">添加</el-button>
                            <el-button type="" size="mini" @click="addReplay(item, false, true)">拒绝</el-button>
                        </div>
                    </div>
                </el-tab-pane>
              </el-tabs>
        </el-dialog>
    </div>
</template>
<script type="text/javascript">
    import {wsRequest} from "../../assets/scripts/ws/apiRequest.js";
    import { mapGetters, mapActions } from 'vuex';
    export default {
        name: 'news',
        data () {
            return {
                showNewsBox: false
            }
        },
        computed: {
            ...mapGetters({
                newsList: 'friendApplyList',
                groupNewsList: 'groupApplyList'
            }),
            unReadClass: function () {
                var list = this.newsList;
                var groupList = this.groupNewsList;
                return ((list.length == 0 && groupList.length == 0) ? 'dn' : '');
            }
        },
        methods: {
            ...mapActions([
                'updateApplyList',
                'updateFriendList',
                'updateGroupList'
            ]),
            showBox: function () {
                this.showNewsBox = true;
            },
            addReplay: function (data, isAdd, isGroup) {
                var that = this;
                var reqInfo = data;
                var reqData = {
                    agree: isAdd
                };
                var url;
                if (isGroup) {
                    reqData["groupApplyId"] = reqInfo.groupApplyId;
                    url = "wss://wchat.thankjava.com/ws/group/add_reply";
                } else {
                    reqData["friendApplyId"] = reqInfo.friendApplyId;
                    url = "wss://wchat.thankjava.com/ws/friend/add_reply";
                }
                wsRequest(
                    url,
                    reqData,
                    function (data) {
                        if (data.code == '0000' && isAdd) {
                          // 更新消息列表
                          reqInfo.addNews = false;
                          reqInfo.agree = isAdd;
                            if (isGroup) {
                                // 处理成功后更新群组列表
                                // that.$store.commit('UPDATE_GROUPSESSIONS_LIST', reqInfo);
                                let tempList = [];
                                let tempItem = {
                                    "groupId": reqInfo.groupApplyId || reqInfo._id,
                                    "groupHeadImg": reqInfo.headImg || reqInfo.groupHeadImg,
                                    "groupName": reqInfo.groupName,           // * 群组帐号
                                    "groupNickname": reqInfo.groupNickname || '' ,        // * 群组名称
                                    "introduction": reqInfo.introduction || '',
                                    "ownerUserId": reqInfo.ownerUserId,
                                    "isRead": true
                                };
                                tempList.push(tempItem)
                                that.updateGroupList({
                                    list: tempList,
                                    type: 1
                                });
                                //更新群请求消息列表
                                that.updateApplyList(true);
                                that.$notify({
                                    title: '提示',
                                    message: "您已同意" + reqInfo.username + "进入" + (reqInfo.groupNickname || reqInfo.groupName),
                                    type: 'success'
                                });
                                that.showNewsBox = false;
                            } else {
                                let tempList = [];
                                // 处理成功后更新朋友列表
                                let tempItem = {
                                    "username": reqInfo.username || '',
                                    "nickname": reqInfo.nickname || '',
                                    "headImg": reqInfo.headImg || '',
                                    "sex": reqInfo.sex || '',
                                    "sign": reqInfo.sign || '',
                                    "remark": reqInfo.remark || '',
                                    'userId': reqInfo.fromUserId,
                                    'status': reqInfo.status || '1',
                                    'isRead': true
                                };
                                tempList.push(tempItem)
                                that.updateFriendList({
                                    list: tempList,
                                    type: 1
                                });

                                // 更新好友请求列表
                                that.updateApplyList(false);
                                that.$notify({
                                    title: '提示',
                                    message: "您已添加" + reqInfo.username + "为好友",
                                    type: 'success'
                                });
                                that.showNewsBox = false;
                            }

                          return false;
                        }
                        if (data.code == '0000' && !isAdd && isGroup) {
                            that.$notify({
                                title: '提示',
                                message: "您已拒绝添加" + data.data.username + "入群",
                                type: 'warning'
                            });
                            that.updateApplyList(true);
                            that.showNewsBox = false;
                            return false;
                        }
                        if (data.code == '0000' && !isAdd)  {
                          that.$notify({
                            title: '提示',
                            message: "您已拒绝添加" + data.data.username + "为好友",
                            type: 'warning'
                          });
                            that.updateApplyList(false);
                            that.showNewsBox = false;
                          return false;
                        }
                        if (data.code != '0000') {
                          isGroup ? that.updateApplyList(true) : that.updateApplyList(false);
                          that.$notify({
                            title: '提示',
                            message: data.desc,
                            type: 'warning'
                          });
                          that.showNewsBox = false;
                        }
                    },
                    function (data) {
                      console.log(data);
                    }
                  );
            }
        }
    }
</script>
<style type="text/css" lang="scss">
.msg-box {
    .el-tabs__nav .el-tabs__item {
        color: #333;
    }
    .el-dialog__body {
        position:relative;
    }
    .friend-news, .group-news {
        position: absolute;
        top: 0.4rem;
        left: 0.7rem;
        color: #FF3B30;
        font-size: 12px;
    }
    .group-news {
        left: 1.65rem;
    }

    .news-box {
        width: 105%;
        max-height: 5rem;
        min-height: 3rem;
        overflow-y: scroll;
    }
    .news-box::-webkit-scrollbar {display:none}
    .news-item {
        display: -webkit-box;
        padding: 0.2rem 0.1rem;
        border-bottom: 1px solid #eee;
        img {
            width: 0.5rem;
            height: 0.5rem;
            margin-right: 0.15rem;
        }
    }
    .dn {
        display: none;
    }
    .msg-icon {
        position: relative;
        margin-right: 0.1rem;
        margin-top: 0.03rem;
        cursor:pointer;
        .el-icon-message {
            font-size: 0.16rem;
        }
        .tip {
            position: absolute;
            font-size: 0.1rem;
            color: #FF3B30;
            top: -0.02rem;
            right: -0.05rem;
        }
    }
    .user-info {
        display: flex;
        flex: 1;
        flex-direction: column;
        justify-content: space-between;
        margin-right: 0.15rem;
    }
    .el-button {
        margin-top: 0.2rem;
    }

}


</style>
