export const user = state => state.user;

export const currentFriend = state => state.currentFriend;

// 当前会话好友id
export const currentFriendId = state => state.currentFriendId;

// 好友会话列表
export const sessions = state=> state.sessions;

// 当前会话列表
export const currentSession = (state) => {
    return state.sessions[state.currentFriendId];
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

// 好友列表
export const friendList = (state) => {
    return state.friendList;
}

// 群组列表
export const groupList = (state) => {
    return state.groupList;
}

export const currentGroupId = state => state.currentGroupId;

export const currentGroupName = state => state.currentGroupName;

export const filterKey = state => state.filterKey;

// 消息列表
export const friendNewsList = state => state.friendNewsList;

export const groupNewsList = state => state.groupNewsList;


// tab类型获取
export const tabType = state => state.tabType;
