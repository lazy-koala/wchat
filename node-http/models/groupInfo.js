/**
 * @Author:acexy@thankjava.com
 * 18/8/20
 * @Description:
 */
const baseModel = require('./basic.js');

class groupInfo extends baseModel {

    Schema() {
        return {
            groupNickname: {type: String, required: true},
            groupName: {type: String, required: true, unique: true},
            ownerUserId: {type: String, required: true},
            introduction: {type: String, required: true},
            headImg: {type: String, required: false},
        };
    };

    SchemaName() {
        return 'group_info';
    };

    save(groupInfo) {
        return new this.model(groupInfo).save();
    }

    selectByGroupName(groupName) {
        return this.model.findOne({groupName: groupName}).exec();
    }

    selectByConditionOnlyOne(friendRelation) {
        return this.model.findOne(friendRelation).exec();
    }

    updateById(id, friendRelation) {
        return this.model.updateOne({_id: baseModel.typeObject(id)}, friendRelation).exec();
    }

    selectByIds(ids) {
        let queryIds = [];
        for (let index in ids) {
            queryIds.push(baseModel.typeObject(ids[index]));
        }
        return this.model.find({_id: {"$in": queryIds}}).exec();
    }

    selectByOwnerUserId(ownerUserId) {
        return this.model.find({ownerUserId: ownerUserId}).exec()
    }

    selectByGroupNicknameOrGroupName(groupName, groupNickname) {
        let query = {};
        if (groupName && groupNickname) {
            query = {"$or": [{groupName: groupName}, {groupNickname: {"$regex": groupNickname, "$options": 'i'}}]};
        } else {
            if (groupName) query.groupName = groupName;
            if (groupNickname) query.groupNickname = {"$regex": groupNickname, "$options": 'i'};
        }
        return this.model.find(query).exec();
    }
}

module.exports = new groupInfo();