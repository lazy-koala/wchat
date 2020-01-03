package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.GroupApply;
import com.thankjava.wchat.db.mongo.GroupApplyMapper;

import java.util.List;

public class GroupApplyMapperImpl implements GroupApplyMapper {

    private final String tableName = MongoTableName.group_apply.value();

    @Override
    public GroupApply selectById(String id) {
        return MONGO_MANAGER.findByObjectId(tableName, id, GroupApply.class);
    }

    @Override
    public String insert(GroupApply groupApply) {
        return MONGO_MANAGER.insertOne(tableName, groupApply);
    }

    @Override
    public List<GroupApply> selectByCondition(GroupApply groupApply) {
        return MONGO_MANAGER.findMany(tableName, groupApply, GroupApply.class);
    }

    @Override
    public boolean updateById(GroupApply groupApply) {
        return MONGO_MANAGER.updateOneByObjectId(tableName, groupApply, groupApply.getId());
    }

    @Override
    public void deleteById(String id) {

    }
}
