/**
 * @Author:acexy@thankjava.com
 * 18/8/20
 * @Description:
 */
const baseModel = require('./basic.js');

class groupApply extends baseModel {

    Schema() {
        return {
            goGroupId: {type: String, required: true},
            applyUserId: {type: String, required: true},
            processed: {type: Boolean},
            remark: {type: String},
        };
    };

    SchemaName() {
        return 'group_apply';
    };

    selectById(id) {
        return this.model.find({_id: baseModel.typeObject(id)}).exec();
    }

    selectByToGroupIdAndUnprocessed(groupId) {
        return this.model.find({toGroupId: groupId, processed: false}).exec();
    }

}

module.exports = new groupApply();