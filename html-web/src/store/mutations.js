import * as types from './mutation-types'

const mutations = {
    [types.SET_FRIENDLIST](state, list) {
        state.friendList = list;
    },

    [types.SET_CURRENT_FRIEND](state, data) {
        state.currentFriend = {
            friendId: data.id,
            friendName: data.name
        }
    },

    [types.GET_USERINFO](state, user) {
        state.user = user;
    },

    [types.UPDATE_TYPE](state, type) {
        state.tabType = type;
    },

    [types.SET_GROUPLIST](state, list) {
        state.groupList = list;
    },

    [types.SET_CURRENT_GROUP](state, data) {
        state.currentGroup = {
            groupId: data.id,
            groupName: data.name
        }
    },

    [types.SET_SESSIONS](state, sessions) {
        state.sessions = sessions;
    },

    [types.SET_GROUP_USER_LIST](state, list) {
        state.groupFriendList = list;
    },

    [types.SET_GROUP_APPLY_LIST](state, list) {
        state.groupApplyList = list;
    },

    [types.SET_FRIEND_APPLY_LIST](state, list) {
        state.friendApplyList = list;
    },

    [types.SET_GROUP_SESSIONS](state, sessions) {
        state.groupSessions =sessions;
    }

}

export default mutations;


