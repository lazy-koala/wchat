/**
 * @Author: acexy@thankjava.com
 * 2018/7/9
 * @Description:
 */
const Router = require('koa-router');
const baseController = require('./baseController');
const userModel = require('../models/user');
const friendApplyModel = require('../models/friendApply');
const groupInfoModel = require('../models/groupInfo');
const groupApplyModel = require('../models/groupApply');
const uploadConfig = require('../config/upload');


module.exports = new Router(

).post('change_pwd', async ctx => {

    let params = ctx.request.body;
    if (!params) return baseController.response400(ctx);
    if (!params.oldPassword || !params.newPassword || !params.verifyPassword) return baseController.response400(ctx);
    if (!baseController.REG.PASSWORD.test(params.password)) return baseController.response400(ctx, '密码不合法');
    if (params.newPassword != params.verifyPassword) return baseController.response400(ctx, '两次密码不一致');
    if (params.oldPassword == params.newPassword) return baseController.response400(ctx, '原密码和新密码相同');
    let authInfo = ctx.state.authInfo;
    let user = await userModel.selectById(authInfo.id);
    if (user.password != util.md5(user.username + params.newPassword)) return baseController.responseWithCode(ctx, baseController.CODE.PASSWORD_ERROR, '原密码错误');
    await userModel.updatePasswordByUsername(user.username, util.md5(user.username + params.newPassword));
    baseController.response(ctx);

}).post('update_uinfo', async ctx => {

    // TODO:

}).get('get_info', async ctx => {
    baseController.response(ctx, await getUserInfo(ctx));
}).get('friend_apply_list', async ctx => {
    let friendApplies = await friendApplyModel.selectByToUserIdAndUnprocessed(ctx.state.authInfo.id);
    if (friendApplies) {
        let list = await toFriendApplyList(friendApplies);
        return baseController.response(ctx, {list: list});
    }
    baseController.response(ctx);
}).get('group_apply_list', async ctx => {
    let groupInfos = await groupInfoModel.selectByOwnerUserId(ctx.state.authInfo.id);
    if (groupInfos) {
        let applyList = [];
        for (let index in groupInfos) {
            let groupApplies = await groupApplyModel.selectByToGroupIdAndUnprocessed(groupInfos[index]._id.toString());
            if (groupApplies && groupApplies.length > 0) {
                for (let i in groupApplies) {
                    let user = await userModel.selectById(groupApplies[i].applyUserId);
                    applyList.push(toGroupApply(user, groupInfos[index], groupApplies[i]));
                }
            }
        }
        if (applyList.length > 0) return baseController.response(ctx, {list: applyList});
    }
    baseController.response(ctx);
}).routes();

const toFriendApplyList = async friendApplies => {
    let list = [];
    for (let index in friendApplies) {
        let object = {};
        object.friendApplyId = friendApplies[index]._id;
        let user = await userModel.selectById(friendApplies[index].applyUserId);
        object.fromUserId = friendApplies[index].applyUserId;
        object.username = user.username;
        object.nickname = user.nickname;
        object.headImg = user.headImg;
        object.sign = user.sign;
        object.sex = user.sex;
        object.remark = friendApplies[index].remark;
        object.createTime = friendApplies[index].createTime;
        list.push(object);
    }
    return list;
};

const getUserInfo = async ctx => {
    let user = await userModel.selectById(ctx.state.authInfo.id);
    return {
        id: user._id,
        username: user.username,
        nickname: user.nickname,
        headImg: user.headImg,
        sign: user.sign,
        sex: user.sex
    };
};
const toGroupApply = (user, groupInfo, groupApply) => {
    let object = {};
    object.groupApplyId = groupApply._id.toString();
    object.fromUserId = groupApply.applyUserId;
    object.remark = groupApply.remark;
    object.groupName = groupInfo.groupName;
    object.groupNickname = groupInfo.groupNickname;
    object.groupHeadImg = groupInfo.groupHeadImg;
    object.username = user.username;
    object.nickname = user.nickname;
    object.headImg = user.headImg;
    object.createTime = groupApply.createTime;
    return object;
};