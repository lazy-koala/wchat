import * as types from './mutation-types';
import Common from "../assets/scripts/common.js";

function findIdx(list, id) {
    return list.findIndex(function(item, index, arr) {
        return item.userId == id || item.groupId == id;
    });
}

function findItem(list, id, key) {
    for (let item in list) {
        if (list[item][key] == id) {
            return list[item];
        }
    }

    return {};
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
/*
    data: {
        type:
        list:
    }
*/
export const updateFriendList = function ({commit, state}, data) {
    let currentList = JSON.parse(JSON.stringify(state.friendList));
    let retList = [];
    switch (data.type) {
        case 0:  //初始化好友列表
            retList = getNewList(currentList, data.list, false);
            commit(types.SET_FRIENDLIST, retList);
            break;
        case 1: // 接受添加好友的请求 || 对方添加你成功
            // 好友列表添加一条
            let item = data.list[0];
            currentList.push(item);
            commit(types.SET_FRIENDLIST, currentList);
            // 会话列表初始化一条数据
            updateSessions({commit, state},{
                id: item.userId,
                name: item.nickname || item.username,
                session: {}
            })
            break;
        case 2: //更新好友在线状态
            let tempData = data.list;
            let friendInfo = findItem(currentList, tempData.fromUserId, 'userId') || {};
            friendInfo.status = tempData.data.status || '0';
            retList = currentList;
            commit(types.SET_FRIENDLIST, retList);
    }
}


// 更新群列表
/*
    data: {
        list:
        type: 0//初始化 1.入群同意||创建群成功
    }
*/
export const updateGroupList = function ({commit, state}, data) {
    let currentList = JSON.parse(JSON.stringify(state.groupList)) || [];
    let retList = [];
    switch (data.type) {
        case 0:  //初始化群组列表
            retList = getNewList(currentList, data.list, true);;
            break;
        case 1: // 申请请求入群统一
            // 群列表添加一条
            retList = currentList.push(data.list[0]);
            // 会话列表初始化一条数据
            updateGroupSessions({commit, state}, {
                id: data.list[0].groupId
            })
            break;
    }
    commit(types.SET_GROUPLIST, retList);
}

// 更新当前会话好友
export const updateCurrentFriend = function ({commit, state}, data) {
    if(data.id != state.currentFriend.friendId) {
        commit(types.SET_CURRENT_FRIEND, data);
    }
    let currentFriendList = JSON.parse(JSON.stringify(state.friendList)) || [];
    let friendInfo = findItem(currentFriendList, data.id, 'userId');
    friendInfo.isRead = true;
    commit(types.SET_FRIENDLIST, currentFriendList);
}

// 获取当前登陆用户的基本信息
export const getUserInfo = function ({commit, state}, user) {
    commit(types.GET_USERINFO, user);
}

// 更新列表页tab选中状态
export const updateType = function ({commit, state}, type) {
    commit(types.UPDATE_TYPE, type);
}

// 更新当前选中群组
// 更新当前选中群组未读状态
export const updateCurrentGroup = function ({commit, state}, data) {
    if(data.id != state.currentGroup.groupId) {
        commit(types.SET_CURRENT_GROUP, data);
    }
    let currentGroupList = JSON.parse(JSON.stringify(state.groupList)) || [];
    let groupInfo = findItem(currentGroupList, data.id, 'groupId');
    groupInfo.isRead = true;
    commit(types.SET_GROUPLIST, currentGroupList);
}

// 更新好友会话消息
/*
    data: type Object
    data = {
        id: ,
        session: {
            content: '',
            date: '',
            self: false
        }
    }
*/
export const updateSessions = function ({commit, state}, data) {
    let currentFriendList = JSON.parse(JSON.stringify(state.friendList)) || [];
    let sessions = JSON.parse(JSON.stringify(state.sessions)) || [];
    let session = data.session || {};
    let tempLen = sessions[data.id] ? sessions[data.id].length : 0;
    if (JSON.stringify(session) == '{}' || tempLen == 0) {
        sessions[data.id] = [];
        sessions[data.id].push(session);
    } else {
       sessions[data.id].splice(tempLen, 0, session);
    }

    // 若接受消息不是当前会话好友的消息，则需要将好友头像上添加未读标记
    if (session.content && !session.self && data.id != state.currentFriend.friendId) {
        let friendInfo = findItem(currentFriendList, data.id, 'userId');
        friendInfo.isRead = false;
        commit(types.SET_FRIENDLIST, currentFriendList);
    }
    let newFriendList = JSON.parse(JSON.stringify(currentFriendList)) || [];
    let currentFriendId = state.currentFriend.friendId;
    // 若当前消息不是当前会话好友的，将接受消息的好友置顶
    if (!currentFriendId || data.id != currentFriendId || currentFriendId != newFriendList[0].id) {
        let indx = findIdx(newFriendList, data.id);
        if (indx != -1) {
            let item = newFriendList[indx];
            newFriendList.splice(indx, 1);
            newFriendList.splice(0, 0, item);
        }
        commit(types.SET_FRIENDLIST, newFriendList);
    }

    commit(types.SET_SESSIONS, sessions);
}

// 更新群消息
export const updateGroupSessions = function ({commit, state}, data) {
    let sessions = JSON.parse(JSON.stringify(state.groupSessions));
    let session = data.session || {};
    let tempLen = sessions[data.id] ? sessions[data.id].length : 0;
    let currentGList = JSON.parse(JSON.stringify(state.groupList));

    if (JSON.stringify(session) == '{}' || tempLen == 0) {
        sessions[data.id] = [];
        sessions[data.id].push(session);
    } else {
        sessions[data.id].splice(tempLen, 0, session);
    }

    // 若接受消息不是当前会话群组的消息，则需要将群组头像上添加未读标记
    if (session.content && data.id != state.currentGroup.groupId) {
        let groupInfo = findItem(currentGList, data.id, 'groupId');
        groupInfo.isRead = false;
        commit(types.SET_FRIENDLIST, currentGList);
    }
    let currentGroupId = state.currentGroup.groupId;
    let newCurrentGList = JSON.parse(JSON.stringify(currentGList));
    // 若当前消息不是当前会话好友的，将接受消息的群置顶
    // 若当前发消息的群不是置顶的，则置顶改群
    if (!currentGroupId || data.id != currentGroupId ||  data.id != newCurrentGList[0].groupId) {
        let idx = findIdx(newCurrentGList, data.id);
        if (idx != -1 && idx != 0) {
            let item = newCurrentGList[idx];
            newCurrentGList.splice(idx, 1);
            newCurrentGList.splice(0, 0, item);
        }

        commit(types.SET_GROUPLIST, newCurrentGList);
    }

    commit(types.SET_GROUP_SESSIONS, sessions);
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

// 更新请求消息列表
export const updateApplyList = function ({commit, state}, isGroup) {
    let url = '';
    isGroup ? url = 'getGroupApplyList' : url = 'getFriendApplyList';
    Common.axios({
        url: url
     }).then((res) => {
        if (!res || !res.data) {
            return;
        }
        let list = res.data.list || [];
        if (isGroup) {
            commit(types.SET_GROUP_APPLY_LIST, list);
        } else {
            commit(types.SET_FRIEND_APPLY_LIST, list);
        }
    });
}


