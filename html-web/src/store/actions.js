import * as types from './mutation-types';

function findIdx(list, id) {
    return list.findIndex(function(item, index, arr) {
        return item.userId == id;
    });
}

function getNewList(currentList, list, isGroup) {
    for(let i = 0; i < list.length; i++) {
        let item = list[i];
        // 好友列表需要初始化小红点状态为空
        item.isRead = true;
        let id = isGroup ? item.groupId : item.userId;
        if(findIdx(currentList, id) == -1) {
            currentList.push(item);
        }
    }
    return currentList;
}

// 更新好友列表
export const updateFriendList = function ({commit, state}, list) {
    let currentList = [...state.friendList] || [];
    let retList = getNewList(currentList, list, false);
    commit(types.SET_FRIENDLIST, retList);
}

// 更新当前会话好友
export const updateCurrentFriend = function ({commit, state}, data) {
    commit(types.SET_CURRENT_FRIEND, data);
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
    let retList = getNewList(currentList, list, true);
    commit(types.SET_GROUPLIST, retList);
}

// 更新当前选中群组
export const updateCurrentGroup = function ({commit, state}, data) {
    commit(types.SET_CURRENT_GROUP, data);
}

// 更新好友会话消息
/*
    data: type Object
    {
        id: '',
        session: {

        }
    }
*/
export const updateSessions = function ({commit, state}, data) {
    let sessions = JSON.parse(JSON.stringify(state.sessions));
    let session = data.session || {};
    let tempLen = sessions[data.id] ? sessions[data.id].length : 0;
    if (JSON.stringify(session) == '{}' && !tempLen) {
        sessions[data.id] = [];
    } else {
        sessions[data.id].splice(tempLen-1, 0, session);
    }
    commit(types.SET_SESSIONS, sessions);
}

// 更新群成员列表
export const updateGroupFriendList = function ({commit, state}, list) {
    let groupFriendList = JSON.parse(JSON.stringify(state.groupFriendList));
    let currentGroupId = state.currentGroup.groupId;
    if (!groupFriendList[currentGroupId]) {
        groupFriendList[currentGroupId] = {};
        for(let item in list) {
            groupFriendList[currentGroupId][list[item].userId] = list[item];
        }
        commit(types.SET_GROUP_USER_LIST, groupFriendList);
    }
}


