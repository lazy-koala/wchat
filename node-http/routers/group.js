/**
 * @Author:acexy@thankjava.com
 * 2018/6/14
 * @Description: 群组模块相关
 */

const Router = require('koa-router');

const router = new Router({
    prefix: '/group/'
});

router.use(require('../controller/group'));

module.exports = router.routes();