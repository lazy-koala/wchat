export const user = state => state.user;

export const currentFriend = state => state.currentFriend;

export const currentFriendId = state => state.currentFriendId;

// 好友会话列表
export const sessions = state=> state.sessions;

// 当前会话列表
export const currentSession = (state) => {
    return state.sessions[state.currentFriendId];
}

// 好友信息
export const friendInfo = (state) => {
    return state.friendList[currentFriend];
}

// 好友列表
export const friendList = (state) => {
    return state.friendList;
}

export const currentGroupId = state => state.currentGroupId;

export const currentGroupName = state => state.currentGroupName;

export const filterKey = state => state.filterKey;

// 消息列表
export const friendNewsList = state => state.friendNewsList;

export const groupNewsList = state => state.groupNewsList;


// tab类型获取
export const tabType = state => state.tabType;
