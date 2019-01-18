import * as types from './mutation-types';

function findIdx(list, id) {
    return list.findIndex(function(item, index, arr) {
        return item.userId == id;
    });
}

// 更新好友列表
export const friendList = function ({commit, state}, list) {
    let currentList = state.friendList || [];
    for(let i = 0; i < list.length; i++) {
        if(findIdx(currentList, list[i].userId) == -1) {
            currentList.push(list[i]);
        }
    }

    commit(types.SET_FRIENDLIST, currentList);
}