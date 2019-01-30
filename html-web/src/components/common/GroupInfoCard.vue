<template>
<div>
    <el-popover
        placement="right"
        ref="infoMore"
        width="200"
        trigger="hover">
        <div style="padding: 10px;">
            <div class="info-wrapper">
                <div class="info-box">
                    <div class="nick-name">
                        <h4></h4>{{groupInfo.groupNickname || groupInfo.groupName }}
                        <!-- <span class="el" :class="sexClass"></span> -->
                    </div>
                    <div v-if="groupInfo.groupName">
                        <span>账号: </span>
                        <span>{{groupInfo.groupName}}</span>
                    </div>
                </div>
                <img class="avatar" :src="groupInfo.groupHeadImg">
            </div>
            <div class="info">
                <div v-if="groupInfo.remark"><span>备注: </span><h4>{{groupInfo.remark}}</h4></div>
                <div v-if="groupInfo.introduction"><span>简介: </span><h4>{{groupInfo.introduction}}</h4></div>
                <!-- <div v-if="user.email"><span>邮箱: </span><h4>{{user.email}}</h4></div> -->
            </div>
        </div>
    </el-popover>
    <div  class="headImg-wrapper">
        <img class="avatar"  v-popover:infoMore :src="groupInfo.groupHeadImg">
        <div class="status-box"><span class="el el-icon-online icon-online" :class="groupInfo.isRead ? 'unRead' : ''"></span></div>
    </div>
</div>
</template>
<style type="text/css" scoped lang="scss">
    .info-wrapper {
        display: flex;
        align-items: center;
        .info-box {
            flex: 1;
        }
        .nick-name {
            flex: 1;
            display: flex;
            align-items: center;
        }
        img {
            width: 0.4rem;
            height: 0.4rem;
        }
        span {
            font-weight: 600;
            padding-right: 0.05rem;
            font-size: 14px;
        }
    }
    .nick-name {
        font-size: 20px;
        font-weight: 700;
    }
    .avatar {
        width: 0.4rem;
        height: 0.4rem;
        border-radius: 0.2rem;
    }
    .list-avatar {
        width: 0.3rem;
        height: 0.3rem;
    }
    .info {
        margin-top: 0.1rem;
        border-top: 2px solid #eee;
        padding-top: 0.1rem;
        font-size: 12px;
        div {
            display: flex;
        }
        span {
            font-weight: 700;
            width: 0.35rem;
        }
        h4 {
            flex: 1;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3;
            overflow: hidden;
        }
    }
    .sex-woman, .sex-man {
        color: #f76358;
        font-size: 14px;
        padding-left: 0.05rem;
    }
    .sex-man {
        color: #409EFF;
    }
    .headImg-wrapper {
        position: relative;
    }
    .headImg-offline {
        filter: grayscale(100%);
    }
    .status-box {
        position: absolute;
        top: -0.05rem;
        right: -0.05rem;
    }
    .icon-online {
        font-size: 15px;
        color: #FF3B30;
    }
    .unRead {
        font-size: 0;
    }
</style>
<script type="text/javascript">
    export default {
        name: 'infoCard',
        props: {
            userId: {
                type: String
            },
            isSelf: {
                type: Boolean,
            },
            groupInfo: {
                type: Object
            }
        }
    }
</script>