export const user = state => state.user;

// 当前会话好友
export const currentFriend = state => state.currentFriend;

// 好友会话列表
export const sessions = state=> state.sessions;

// 当前好友会话列表
export const currentSession = (state) => {
    return state.sessions[state.currentFriend.friendId];
}

// 好友信息
export const friendInfo = (state) => (id) => {
    let friendList = state.friendList;
    for(let item in friendList) {
        if (friendList[item].userId == id) {
            return friendList[item];
        }
    }

    return state.user;
}

// 群组信息
export const GroupInfo = (state) => (id) => {
    let groupList = state.groupList;
    for(let item in groupList) {
        if (groupList[item].groupId == id) {
            return groupList[item];
        }
    }
}

// 好友列表
export const friendList = (state) => {
    return state.friendList;
}

// 群组列表
export const groupList = (state) => {
    return state.groupList;
}

// 当前会话群组聊天信息
export const currentGroupSession = (state) => {
    return state.groupSessions[state.currentGroup.groupId];
}

// 当前会话群组id和name
export const currentGroup = state => state.currentGroup;

// 当前会话全组的好友列表
export const currentFriendList = (state) => (id) => {
    let friendList = state.groupFriendList;
    for(let item in friendList) {
        if (item == id) {
            return friendList[item];
        }
    }
}

// 某群组好友列表
export const hasGroupFriendList = (state) => (groupId) => {
    let groupFriend = state.groupFriendList[groupId] || {};
    if(JSON.stringify(groupFriend) == '{}') {
        return false;
    } else {
        return true;
    }
}

export const filterKey = state => state.filterKey;

// tab类型获取
export const tabType = state => state.tabType;


// 好友申请列表
export const friendApplyList = state => state.friendApplyList;

// 申请加群列表
export const groupApplyList = state => state.groupApplyList;