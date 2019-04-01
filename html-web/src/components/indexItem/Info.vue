<template>
    <div class="user">
        <header>
            <div class="user-info">
                <info-card :userId="userId" :isSelf="isSelf"></info-card>
                <p class="name">{{user.nickname}}</p>
            </div>
            <news></news>
            <search-friend></search-friend>
            <create-group></create-group>
        </header>
        <p class="sign">{{user.sign}}</p>
        <footer>
            <!-- <input class="search" type="text" placeholder="search user..." @keyup="onKeyup"> -->
        </footer>
    </div>
</template>
<script type="text/javascript">
import $axios from 'axios';
import Common from "../../assets/scripts/common.js";
import InfoCard from '../common/InfoCard';
import News from '../common/News';
import SearchFriend from '../SearchFriend';
import CreateGroup from '../CreateGroup';
import { mapActions, mapGetters } from 'vuex';

export default {
    name: 'User',
    data() {
        return {
            userId: '',
            isSelf: true
        }
    },
    components: {
        'info-card': InfoCard,
        'search-friend': SearchFriend,
        'news': News,
        'create-group': CreateGroup
    },
    methods: {
        onKeyup (e) {
        },

        ...mapActions([
            'getUserInfo',
            'updateApplyList'
        ])
    },
    computed: {
        ...mapGetters([
            'user'
        ])
    },
    created() {
        let that = this;

        // 获取本人基本信息
        Common.axios({
            url: 'getUserInfo'
        }).then((res) => {
            if (res && res.data) {
                let userInfo = res.data || {};
                userInfo.isRead = true;
                that.userId = userInfo.id;
                that.getUserInfo(userInfo);
            } else {
                that.$message({
                    message: res.message,
                    type: 'warning'
                });
                return false;
            }
        }, (error) => {

        });
    }
}
</script>
<style type="text/css" lang="scss" scoped>
    .user {
        padding: 0.12rem 0.1rem 0rem 0.12rem;
        border-bottom: solid 1px #24272C;
    }
    header {
        display: flex;
        align-items: center;
    }
    .user-info {
        flex: 1;
        display: flex;
        align-items: center;
    }
    .sign {
        margin-top: 0.1rem;
        font-size: 12px;
        line-height: 0.15rem;
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
    }
    .name {
        display: inline-block;
        margin: 0 0 0 0.15rem;
        font-size: 0.14rem;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        width: 0.6rem;
    }
    .search {
        padding: 0 0.1rem;
        font-size: 12px;
        color: #fff;
        height: 0.3rem;
        line-height: 0.3rem;
        border: solid 1px #3a3a3a;
        border-radius: 0.04rem;
        outline: none;
        background-color: #26292E;
    }
    footer {
        margin-top: 0.1rem;
    }
</style>