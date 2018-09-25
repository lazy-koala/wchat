/**
 * @Author:acexy@thankjava.com
 * 18/8/20
 * @Description:
 */
const baseModel = require('./basic.js');

class friendApply extends baseModel {

    Schema() {
        return {
            toUserId: {type: String, required: true},
            applyUserId: {type: String, required: true},
            agree: {type: Boolean, required: false},
            processed: {type: Boolean, required: false},
            remark: {type: String, required: false},
        };
    };

    SchemaName() {
        return 'friend_apply';
    };

    selectByUserId(userId) {
        return this.model.find({toUserId: userId}).exec();
    }

    selectByToUserIdAndUnprocessed(toUserId) {
        return this.model.find({toUserId: toUserId, processed: false}).exec();
    }

    selectByConditionOnlyOne(friendRelation) {
        return this.model.findOne(friendRelation).exec();
    }

    updateById(id, friendRelation) {
        return this.model.updateOne({_id: baseModel.typeObject(id)}, friendRelation).exec();
    }
}

module.exports = new friendApply();