/**
 * @Author:acexy@thankjava.com
 * 18/8/20
 * @Description:
 */
const baseModel = require('./basic.js');

class friendRelation extends baseModel {

    Schema() {
        return {
            userId: {type: String, required: true},
            friendUserId: {type: String, required: true},
            remark: {type: String, required: false},
        };
    };

    SchemaName() {
        return 'friend_relation';
    };


    selectByUserId(userId) {
        return this.model.find({userId: userId}).exec();
    }

    selectByConditionOnlyOne(friendRelation) {
        return this.model.findOne(friendRelation).exec();
    }

    updateById(id, friendRelation) {
        return this.model.updateOne({_id: baseModel.typeObject(id)}, friendRelation).exec();
    }
}

module.exports = new friendRelation();