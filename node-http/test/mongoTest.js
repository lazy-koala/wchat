/**
 * @Author:acexy@thankjava.com
 * 18/8/24
 * @Description:
 */
/**
 * @Author: acexy@thankjava.com
 * 2018/6/15
 * @Description:
 */
require('../extends');
const mongodb = require('../lib/mongodb');
const util = require('../lib/util');




// util.fsDel(['/Users/acexy/Downloads/a'])



mongodb.open(function () {

    const userModel = require('../models/user');

    userModel.save({
        username: 'acexy1',
        password: util.md5('acexy1123456'),
        nickname: 'acexy1',
        sign:'测试帐号',
        sex:'1',
        headImg:"https://source.thankjava.com/2018/8/24/942fe2bdf151c0a0d22ff6aaf82aeae6.png"
    }).then(resolve => {
        console.log(resolve)
    });

    // userModel.updatePasswordByUsername('a1','bbb').then(resolve =>{
    //     console.log(resolve)
    // });

    // userModel.selectById("5b440ec6fbd7373780ebefba").then(resolve => {
    //    console.log(resolve)
    // });

    // userModel.selectByConditionOnlyOne({
    //     username:'acexy',
    //     email:'acexy@thankjavas.com'
    // }).then(resolve => {
    //     console.log(resolve)
    // })

    //
    // userModel.selectByUsernameOrEmail('acexy').then((doc) => {
    //     console.log(doc)
    // })


    // const authTokenModel = require('../models/authToken');
    // authTokenModel.save({
    //     userId: '123',
    //     expiration: 1,
    // });
    //
    // authTokenModel.deleteById('5b2895b9cac99a1ac1a3d9ce').then(resolve=>{
    //     console.log(resolve)
    // })


    // const imagesModel = require('../models/images');
    // imagesModel.saveMany([{userId: '1', uri: "/1"}, {userId: '2', uri: '/2'}]).then(resolve => {
    //     console.log(resolve);
    // });
    // imagesModel.selectByPage({pageSize:2,pageNumber:1,query:{userId:'5b440f12ea87fa29286aea5c'}}).then(resolve => {
    //     console.log(resolve)
    // })
    // imagesModel.selectByPage({
    //     pageSize: 2,
    //     pageNumber: 4
    // }).then(resolve => {
    //     console.log(resolve)
    // })
    // imagesModel.removeOwnManyById(['5b2b934420c9152b63945b68','5b2b934420c9152b63945b69'],'1').then(resolve => {
    //     console.log(resolve);
    // })
    // imagesModel.selectOwnByIds(['5b2b935f892c692b66675ce4'], '1').then(resolve => {
    //     console.log(resolve)
    // });


});
