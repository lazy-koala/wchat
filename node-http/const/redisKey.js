/**
 * @Author:acexy@thankjava.com
 * 2018/6/1
 * @Description: redisKey管理
 */
const util = require('util');

module.exports.AUTH_TOKEN = function (token) { // string
    return util.format('wchat:token:%s', token);
}; // 用户token信息鉴权缓存

module.exports.USER_ONLINE = function() {
  return 'wchat:user:online';
};