import * as types from './mutation-types';

function findIdx(list, id) {
    return list.findIndex(function(item, index, arr) {
        return item.userId == id;
    });
}

function getNewLsit(currentLsit, list, isGroup) {
    for(let i = 0; i < list.length; i++) {
        // 初始化小红点状态为空
        isGroup ? list[i].isRead = true : '';
        if(findIdx(currentList, list[i].userId) == -1) {
            currentList.push(list[i]);
        }
    }
    return currentList;
}

// 更新好友列表
export const updateFriendList = function ({commit, state}, list) {
    let currentList = [...state.friendList] || [];
    let retList = this.getNewLsit(currentList, list, false);
    commit(types.SET_FRIENDLIST, retList);
}

// 更新当前会话好友currentId
export const updateCurrentId = function ({commit, state}, id) {
    commit(types.SET_CURRENTID, id);
}

// 获取当前登陆用户的基本信息
export const getUserInfo = function ({commit, state}, user) {
    commit(types.GET_USERINFO, user);
}

// 更新列表页tab选中状态
export const updateType = function ({commit, state}, type) {
    commit(types.UPDATE_TYPE, type);
}

// 更新群列表信息
export const updateGroupList = function ({commit, state}, list) {
    let currentList = [...state.groupList] || [];
    let retList = this.getNewLsit(currentList, list, true);
    commit(types.SET_FRIENDLIST, retList);
}

// 更新当前选中群组
export const updateCurrentGroupId = function ({commit, state}, id) {
    commit(types.SET_GROUPID, id);
}


