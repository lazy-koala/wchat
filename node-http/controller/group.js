const Router = require('koa-router');
const groupInfoModel = require('../models/groupInfo');
const groupRelationModel = require('../models/groupRelation');
const userModel = require('../models/user');
const baseController = require('./baseController');
const baseConfig = require('../config/basic');

module.exports = new Router(

).post('create', async ctx => {

    let params = ctx.request.body;

    if (!params) return baseController.response400(ctx);
    if (!params.groupName || !params.groupNickname || !params.introduction) return baseController.response400(ctx);
    if (!baseController.REG.USERNAME.test(params.groupName)) return baseController.response400(ctx, '群帐号不合法');

    let groupInfo = await groupInfoModel.selectByGroupName(params.groupName);
    if (groupInfo) return baseController.responseWithCode(ctx, baseController.CODE.EXISTING_GROUP_NAME, "该群账号已存在");
    params.ownerUserId = ctx.state.authInfo.id;
    params.headImg = baseConfig.headImgGroup;
    groupInfo = await groupInfoModel.save(params);

    if (!groupInfo) return baseController.response500(ctx);

    let groupId = groupInfo._id.toString();

    await groupRelationModel.save({
        groupId: groupId,
        userId: ctx.state.authInfo.id
    });

    baseController.response(ctx, {
        groupId: groupId,
        groupHeadImg: groupInfo.headImg,
        groupName: groupInfo.groupName,
        groupNickname: groupInfo.groupNickname,
        introduction: groupInfo.introduction,
        createTime: groupInfo.createTime
    });

}).get("search", async ctx => {

    let param = ctx.query;
    if (!param.groupNickname && !param.groupName) return baseController.response400(ctx);
    let groups = await groupInfoModel.selectByGroupNicknameOrGroupName(param.groupName, param.groupNickname);
    if (groups && groups.length > 0) {
        return baseController.response(ctx, {list: group2Objects(groups)});
    }
    baseController.response(ctx);

}).get('get_group_list', async ctx => {

    let param = ctx.query;

    if (!param) return baseController.response400(ctx);
    if (param.type == '1') {
        let groupRelations = await groupRelationModel.selectByUserId(ctx.state.authInfo.id);
        if (groupRelations.length > 0) {
            let ids = [];
            for (let index in groupRelations) {
                ids.push(groupRelations[index].groupId);
            }
            let groupInfos = await groupInfoModel.selectByIds(ids);
            if (groupInfos.length > 0) {
                let allGroup = [];
                for (let index in groupInfos) {
                    let group = group2Object(groupInfos[index]);
                    let owner;
                    if (groupInfos[index].ownerUserId == ctx.state.authInfo.id) {
                        owner = ctx.state.authInfo;
                    } else {
                        owner = await userModel.selectById(groupInfos[index].ownerUserId);
                    }
                    group.ownerUserId = owner._id;
                    group.ownerUername = owner.username;
                    group.ownerNickname = owner.nickname;
                    group.ownerHeadImg = owner.headImg;
                    group.ownerSex = owner.sex;
                    group.ownerSign = owner.sign;
                    allGroup.push(group);
                }
                if (allGroup.length > 0) return baseController.response(ctx, {list: allGroup});
            }
        }
    } else if (param.type == '2') {
        let groupInfos = await groupInfoModel.selectByOwnerUserId(ctx.state.authInfo.id);
        if (groupInfos) {
            return baseController.response(ctx, {list: group2Objects(groupInfos)});
        }
    } else {
        return baseController.response400(ctx);
    }

    baseController.response(ctx);

}).get('get_group_user_list', async ctx => {
    let param = ctx.query;
    if (!param || !param.groupId) return baseController.response400(ctx);
    let data = {};
    if (Array.isArray(param.groupId)) {
        let groupRelations;
        for (let index in param.groupId) {
            groupRelations = await groupRelationModel.selectByGroupId(param.groupId[index]);
            if (groupRelations) {
                let users = [];
                for (let index in groupRelations) {
                    let user = await userModel.selectById(groupRelations[index].userId);
                    user = user2Object(user);
                    user.remark = groupRelations[index].remark;
                    users.push(user);
                }
                data[param.groupId[index]] = users;
            } else {
                data[param.groupId[index]] = [];
            }
        }
    } else {
        let groupRelations = await groupRelationModel.selectByGroupId(param.groupId);
        if (groupRelations) {
            let users = [];
            for (let index in groupRelations) {
                let user = await userModel.selectById(groupRelations[index].userId);
                user = user2Object(user);
                user.remark = groupRelations[index].remark;
                users.push(user);
            }
            data[param.groupId] = users;
        } else {
            data[param.groupId] = [];
        }
    }

    baseController.response(ctx, data);

}).routes();

const group2Object = group => {
    return {
        groupId: group._id.toString(),
        groupName: group.groupName,
        groupNickname: group.groupNickname,
        groupHeadImg: group.headImg,
        introduction: group.introduction,
        ownerUserId: group.ownerUserId
    };
};

const group2Objects = (group) => {
    let list = [];
    for (let index in group) {
        list.push(group2Object(group[index]));
    }
    return list;
};

const user2Object = user => {
    return {
        userId: user._id.toString(),
        username: user.username,
        nickname: user.nickname,
        headImg: user.headImg,
        sex: user.sex,
        sign: user.sign,
    };
};
