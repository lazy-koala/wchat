/**
 * @Author:acexy@thankjava.com
 * 18/8/20
 * @Description:
 */
const baseModel = require('./basic.js');

class groupRelation extends baseModel {

    Schema() {
        return {
            groupId: {type: String, required: true},
            userId: {type: String, required: true},
            remark: {type: String, required: false},
        };
    };

    SchemaName() {
        return 'group_relation';
    };

    save(groupRelation) {
        return new this.model(groupRelation).save();
    }

    selectByUserId(userId) {
        return this.model.find({userId: userId}).exec();
    }

    selectByGroupId(groupId) {
        return this.model.find({groupId: groupId}).exec();
    }

}

module.exports = new groupRelation();