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
                        <h4></h4>{{user | name}}
                        <span class="el" :class="sexClass"></span>
                    </div>
                    <div v-if="user.username">
                        <span>账号: </span>
                        <span>{{user.username}}</span>
                    </div>
                </div>
                <img class="avatar" :src="user.headImg">
            </div>
            <div class="info">
                <div v-if="user.remark"><span>备注: </span><h4>{{user.remark}}</h4></div>
                <div v-if="user.sign"><span>签名: </span><h4>{{user.sign}}</h4></div>
                <div v-if="user.email"><span>邮箱: </span><h4>{{user.email}}</h4></div>
            </div>
        </div>
    </el-popover>
    <div  class="headImg-wrapper">
        <img class="avatar"  v-popover:infoMore  :class="avatarClass"  :src="user.headImg">
        <div class="status-box"><span class="el el-icon-online icon-online" :class="(user.isRead && !user.isSelf) ? 'read' : ''"></span></div>
    </div>
</div>
</template>

<script type="text/javascript">
import { mapActions, mapGetters } from "vuex";

    export default {
        name: 'infoCard',
        props: {
            userId: {
                type: String
            },
            isSelf: {
                type: Boolean,
            }
        },
        filters: {
            name: function (user) {
                let name = '';
                user.nickname ? name = user.nickname : name = user.username;
                return name;
            }
        },
        computed: {
            user: function () {
                return this.$store.getters.friendInfo(this.userId);
            },
            sexClass: function () {
                let sex = this.user.sex;
                if (sex == 0) {
                    return 'sex-woman el-icon-woman'
                }
                if (sex == 1) {
                    return 'sex-man el-icon-nan'
                }
            },
            avatarClass: function () {
                let className = '';
                if (!this.isSelf) {
                    className = 'list-avatar ';
                }

                if (this.user.status == '0') {
                    className = className + 'headImg-offline';
                }
                return className;
            }
        }
    }
</script>
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
        width: 0.4rem;
        height: 0.4rem;
        border-radius: 0.2rem;
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
    .read {
        font-size: 0;
    }
</style>