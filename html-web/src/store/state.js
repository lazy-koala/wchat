// import {loadSearch, loadPlay, loadFavorite} from "common/js/cache"

const state = {
    // 当前登陆用户信息
    user: {},

    // 当前会好好友
    currentFriend: {

        // currentFriend: '',

        // 当前选中的会话的用户Id
        // currentFriendId: '',
    },

    // 好友会话列表
    /** 字段注释
        'userId': [
            {
                content: '',
                date: '',
                self: true,
                code: ''
            },
            {
                content: '',
                date: '',
                self: true,
                code: ''
            }

        ]
    **/
    sessions: {},

    // 好友列表
    /*
        "username": "111",          // * 账号
        "nickname": "111",          // * 昵称
        "email": "111@thankjava.com",             // * 电子邮箱（脱敏）
        "headImg": "./static/test.jpg",
        "sex": "0",                      // * 性别 0-女 1-男 -1-未设置
        "sign": "",                     // * 个性签名
        "remark": "",
        'id': ''
    */
    friendList: [],

    // 群列表
    groupList: [],

    // 群会话列表
    groupSessions: {},

    // 当前选中的会话群组
    currentGroupId: '',
    currentGroupName: '',

    // 群成员列表
    groupUserList: {},

    // 过滤出只包含这个key的会话
    filterKey: '',
    friendNewsList: [],
    groupNewsList: [],
    tabType: '0', //0 代表好友列表 1代表群组
}

export default state;