const Router = require('koa-router');
const friendRelationModel = require('../models/friendRelation');
const userModel = require('../models/user');
const asyncRedisClient = require('../lib/asyncRedis').client;
const redisKey = require('../const/redisKey');
const baseController = require('./baseController');

module.exports = new Router(

).post('set_remark', async ctx => {

    let params = ctx.request.body;
    if (!params) return baseController.response400(ctx);
    if (!params.friendUserId || !params.remark) return baseController.response400(ctx);
    let friendRelation = await friendRelationModel.selectByConditionOnlyOne({
        userId: ctx.state.authInfo.id,
        friendUserId: params.friendUserId
    });
    if (!friendRelation) return baseController.responseWithCode(ctx, baseController.CODE.NOT_YOUR_FRIEND);
    await friendRelationModel.updateById(friendRelation._id, {remark: params.remark});
    baseController.response(ctx);

}).get("search_user", async ctx => {

    let param = ctx.query;
    if (!param.nickname && !param.username) return baseController.response400(ctx);
    let users = await userModel.selectByNicknameOrUsername(param.username, param.nickname);
    if (users) {
        return baseController.response(ctx, {list: user2Objects(users)});
    }
    baseController.response(ctx);

}).get('get_friend_list', async ctx => {

    let friendRelations = await friendRelationModel.selectByUserId(ctx.state.authInfo.id);
    if (friendRelations) {
        let ids = [];
        for (let index = 0, len = friendRelations.length; index < len; index++) {
            ids.push(friendRelations[index].friendUserId);
        }
        let users = await userModel.selectByIds(ids);
        let list = [];
        if (users) {
            let user;
            for (let index = 0, len = users.length; index < len; index++) {
                user = user2Object(users[index]);
                user.remark = friendRelations[index].remark;
                user.status = String(await asyncRedisClient.sismemberAsync(redisKey.USER_ONLINE(), users[index]._id.toString()));
                list.push(user);
            }
            baseController.response(ctx, {list: list});
            return;
        }
    }
    baseController.response(ctx);

}).routes();

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

const user2Objects = (users, friendRelations) => {
    let list = [];
    let user;
    for (let index in users) {
        user = user2Object(users[index]);
        if (friendRelations) {
            user.remark = friendRelations[index].remark;
        }
        list.push(user);
    }
    return list;
};