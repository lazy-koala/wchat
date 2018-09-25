/**
 * @Author: acexy@thankjava.com
 * 2018/8/28
 * @Description:
 */
const asyncRedisClient = require('../lib/asyncRedis').client;
const redisKey = require('../const/redisKey');

const f = async function () {
   return await asyncRedisClient.smembersAsync(redisKey.USER_ONLINE());
};


f().then(resolve=>{
   console.log(resolve)
});