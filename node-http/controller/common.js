/**
 * @Author:acexy@thankjava.com
 * 2018/6/13
 * @Description:登录相关处理
 */

const Router = require('koa-router');

const cookiesName = require('../const/cookiesName');
const baseController = require('./baseController');
const util = require('../lib/util');
const asyncRedisClient = require('../lib/asyncRedis').client;
const redisKey = require('../const/redisKey');
const userModel = require('../models/user');
const baseConfig = require('../config/basic');

module.exports = new Router(

).post('login', async (ctx) => {

    // 登录
    let params = ctx.request.body;
    if (!params || Object.keys(params).length == 0) {
        return baseController.response400(ctx);
    }

    let username = params.username;
    let password = params.password;

    if (!username || !password) {
        return baseController.response400(ctx, '帐号或密码为空');
    }

    let user = await userModel.selectByUsername(username);
    if (!user) {
        return baseController.responseWithCode(ctx, baseController.CODE.INVALID_ACCOUNT, '账号不存在');
    }

    if (util.md5(user.username + password) != user.password) {
        return baseController.responseWithCode(ctx, baseController.CODE.PASSWORD_ERROR, '密码错误');
    }

    await doLogin(ctx, user);

    baseController.response(ctx);

}).post('registe', async ctx => {

    let params = ctx.request.body;

    if (!params) return baseController.response400(ctx);
    if (!params.username || !params.password || !params.nickname) {
        return baseController.response400(ctx);
    }

    if (!baseController.REG.USERNAME.test(params.username)) return baseController.response400(ctx, '用户名不合法');
    if (!baseController.REG.PASSWORD.test(params.password)) return baseController.response400(ctx, '密码不合法');

    let user = await userModel.selectByUsername(params.username);
    if (user) return baseController.responseWithCode(ctx, baseController.CODE.EXISTING_USER, '用户账号已存在');

    user = await userModel.save({
        username: params.username,
        password: util.md5(params.username + params.password),
        nickname: params.nickname,
        sex: params.sex ? params.sex : '-1',
        sign: params.sign,
        headImg: params.sex == '0' ? baseConfig.headImgGirl : (params.sex == '1' ? baseConfig.headImgBoy : baseConfig.headImgUnknown)
    });

    await doLogin(ctx, user);
    baseController.response(ctx);

}).routes();


const doLogin = async (ctx, user) => {
    let nowTime = Date.now();
    let token = util.md5(util.uuid() + nowTime + user.username);

    let userInfo = {
        id: user._id.toString(),
        username: user.username,
        nickname: user.nickname,
        headImg: user.headImg,
        sign: user.sign,
        sex: user.sex
    };

    let userInfoJsonStr = JSON.stringify(userInfo);

    let authEx = baseController.CONSTS.AUTH_COOKIE_EXPIRES_DAY * 24 * 60 * 60;

    // 写入redis验证数据
    await asyncRedisClient.setAsync(redisKey.AUTH_TOKEN(token), userInfoJsonStr, 'EX', authEx);

    // 登录成功
    let cookieOpt = {
        httpOnly: true,
        path: '/',
        domain: baseConfig.domain
    };

    cookieOpt.maxAge = authEx * 1000;

    // 创建cookies会话凭证信息
    baseController.setCookie(ctx, cookiesName.COOKIE_NAME_TOKEN, token, cookieOpt);

    cookieOpt.httpOnly = false;

    baseController.setCookie(ctx, cookiesName.COOKIE_NAME_UID, userInfo.id, cookieOpt);

};