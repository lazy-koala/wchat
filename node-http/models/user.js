/**
 * @Author: acexy@thankjava.com
 * 2018/6/15
 * @Description:用户模块数据库
 */

const baseModel = require('./basic.js');

class user extends baseModel {

    Schema() {
        return {
            username: {type: String, required: true, unique: true},
            password: {type: String, required: true},
            nickname: {type: String, required: true},
            headImg: {type: String, required: false},
            sign: {type: String, required: false},
            sex: {type: String, required: false, default: "-1"}
        };
    };

    SchemaName() {
        return 'user';
    };

    save(user) {
        return new this.model(user).save();
    }

    /**
     * 通过帐号查询用户
     * @param username
     * @returns {Promise}
     */
    selectByUsername(username) {
        return this.model.findOne({username: username}).exec();
    }

    /**
     * 条件查询用户信息
     * @param condition
     * @returns {Promise}
     */
    selectByConditionOnlyOne(condition) {
        return this.model.findOne(condition).exec();
    }

    /**
     * 用于搜索用户
     * @param username
     * @param nickname
     * @returns {Promise}
     */
    selectByNicknameOrUsername(username, nickname) {
        let query = {};
        if (username && nickname) {
            query = {"$or": [{username: username}, {nickname: {"$regex": nickname, "$options": 'i'}}]};
        } else {
            if (username) query.username = username;
            if (nickname) query.nickname = {"$regex": nickname, "$options": 'i'};
        }
        return this.model.find(query).exec();
    }

    /**
     * 通过帐号更新密码
     * @param username
     * @param password
     * @returns {Promise}
     */
    updatePasswordByUsername(username, password) {
        return this.model.updateOne({username: username}, {password: password}).exec();
    }

    selectById(id) {
        return this.model.findOne({_id: baseModel.typeObject(id)}).exec();
    }

    selectByIds(ids) {
        let queryIds = [];
        for (let index in ids) {
            queryIds.push(baseModel.typeObject(ids[index]));
        }
        return this.model.find({_id: {"$in": queryIds}}).exec();

    }

    updateByUsername(condition, username) {
        return this.model.updateOne({username: username}, condition).exec();
    }
}

module.exports = new user();