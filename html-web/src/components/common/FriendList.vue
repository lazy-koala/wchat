<template>
<div class="friendList">
    <el-popover
        placement="top-end"
        width="200"
        ref="friendlist"
        trigger="click">
        <div style="padding: 10px;">
            <ul class="list-wrapper">
                <li class="friend-li" v-for="item in friendList">
                    <img :src="item.headImg" class="avatar">
                    <p class="name">{{item.nickname || item.username}}</p>
                </li>
            </ul>
        </div>
    </el-popover>
    <span class="el el-icon-more more" v-popover:friendlist></span>
</div>
</template>
<style type="text/css" lang="scss" scoped>

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
    .more {
        line-height: 0.5rem;
        padding-right: 0.15rem;
        font-size: 0.25rem;
    }
    .list-wrapper::-webkit-scrollbar {
        display: none;
    }
    .friend-li {
        display: flex;
        align-items: center;
        height: 0.5rem;
        border: none;
        cursor: pointer;
        // transition: background-color .1s;
        padding-left: 0.1rem;
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



</style>
<script type="text/javascript">
    export default {
        name: 'friendList',
        props: {
            groupId: {
                type: String,
                default: ''
            }
        },
        computed: {
            friendList: function () {
                return this.$store.getters.currentFriendList(this.groupId) || [];
            }
        }
    }
</script>