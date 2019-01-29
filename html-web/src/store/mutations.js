import * as types from './mutation-types'

const mutations = {
    [types.SET_FRIENDLIST](state, list) {
        state.friendList = list;
    },

    [types.SET_CURRENTID](state, id) {
        state.currentFriendId = id;
    },

    [types.GET_USERINFO](state, user) {
        state.user = user;
    },

    [types.UPDATE_TYPE](state, type) {
        state.tabType = type;
    },

    [types.SET_GROUPID](state, id) {
        state.currentGroupId = id;
    }

}

export default mutations;


