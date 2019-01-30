// import {loadSearch, loadPlay, loadFavorite} from "common/js/cache"

const state = {
    // 当前登陆用户信息
    user: {},

    // 当前会好好友
    currentFriend: {
        // 当前选中的会话用户id
        // friendId: '',

        // 当前选中的会话用户名称
        // friendName: '',
    },

    // 好友会话列表
    /** 字段注释
        'userId': [
            {
                content: '',
                date: 1548830741228,
                self: true,
                code: ''
            },
            {
                content: '',
                date: 1548830741228,
                self: true,
                code: ''
            }

        ]
    **/
    sessions: {
        '111': [
            {
                content: 'testtesttesttesttesttesttesttest',
                date: 1548830741228,
                self: true,
                code: ''
            },
            {
                content: 'testtesttesttesttesttesttesttest',
                date: 1548830745228,
                self: false,
                code: ''
            }
        ]
    },

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
    groupSessions: {
        'groupIdA': [
            {
                content: 'testtesttesttesttesttesttesttest',
                date: 1548830741228,
                self: false,
                userId: '111',
                code: ''
            },
            {
                content: 'testtesttesttesttesttesttesttest',
                date: 1548830745228,
                self: true,
                userId: '222',
                code: ''
            }
        ]
    },

    // 当前选中的会话群组
    currentGroup: {
        // groupId: '',
        // groupName: '',
    },


    // 群成员列表
    /*{
        'groupId': {
            userId1: {
                "userId": "111",
                "username": "111",
                "nickname": "111",
                "headImg": "",
                "sex": "0",
                "sign": "lalla",
                "remark": "111"
            },
            userId1: {

            }...
        }

    }*/
    groupFriendList: {},

    // 过滤出只包含这个key的会话
    filterKey: '',
    friendNewsList: [],
    groupNewsList: [],
    tabType: '0', //0 代表好友列表 1代表群组
}

export default state;