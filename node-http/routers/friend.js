/**
 * @Author:acexy@thankjava.com
 * 2018/6/14
 * @Description: 好友模块相关
 */

const Router = require('koa-router');

const router = new Router({
    prefix: '/friend/'
});

router.use(require('../controller/friend'));

module.exports = router.routes();