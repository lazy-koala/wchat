// import {loadSearch, loadPlay, loadFavorite} from "common/js/cache"

const state = {
    // 当前用户信息
    user: {},

    // 当前会好好友
    currentUser: '',

    // 当前选中的会话Id
    currentSessionId: '',

    // 好友会话列表
    sessions: [],

    // 好友列表
    friendList: {},

    // 群会话列表
    groupSessions: [],

    // 当前选中的会话群组
    currentGroupId: '',
    currentGroupName: '',

    // 群成员列表
    groupUserList: {}

    // 过滤出只包含这个key的会话
    filterKey: '',
    friendNewsList: [],
    groupNewsList: [],
    tabType: '0', //0 代表好友列表 1代表群组
}

export default state;