<template>
    <div class="user">
        <header>
            <div class="user-info">
                <info-card :userId="userId" :isSelf="isSelf"></info-card>
                <p class="name">{{user.nickname}}</p>
            </div>
            <!-- <news></news>
            <search-friend></search-friend>
            <create-group></create-group> -->
        </header>
        <p class="sign">{{user.sign}}</p>
        <footer>
            <!-- <input class="search" type="text" placeholder="search user..." @keyup="onKeyup"> -->
        </footer>
    </div>
</template>
<script type="text/javascript">
import $axios from 'axios';
import InfoCard from '../common/InfoCard';
import News from '../common/News';
import SearchFriend from '../SearchFriend';
import CreateGroup from '../CreateGroup';
import { mapActions, mapGetters } from "vuex";

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
            'getUserInfo'
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
        $axios.get('/api/user/get_info', {}).then((res) => {
            if (res && res.data && res.data.data) {
                let userInfo = res.data.data || {};
                userInfo.isRead = true;
                that.userId = userInfo.id;
                that.getUserInfo(userInfo);
            } else {
                that.$message({
                    message: res.data.message,
                    type: 'warning'
                });
                return false;
            }
        });
        // 获取未处理申请列表
        $axios.get('/api/user/friend_apply_list', {}).then((res) => {
            if (res && res.data && res.data.data) {
                let newsList = res.data.data.list || [];
                localStorage.setItem('news-list', newsList);
                that.$store.commit('INIT_NEWS_LIST', newsList);
            }
        });

        // 获取未处理的群申请列表
        $axios.get('/api/user/group_apply_list', {}).then((res) => {
            if (res && res.data && res.data.data) {
                let newsGroupList = res.data.data.list || [];
                localStorage.setItem('news-group-list', newsGroupList);
                that.$store.commit('INIT_GROUP_NEWS_LIST', newsGroupList);
            }
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
        font-size: 0.16rem;
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