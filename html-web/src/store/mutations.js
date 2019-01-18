import * as types from './mutation-types'

const mutations = {
    [types.SET_FRIENDLIST](state, list) {
        state.friendList = list;
    }

}

export default mutations;


