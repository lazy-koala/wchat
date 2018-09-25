package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.GroupApply;
import com.thankjava.wchat.db.mongo.GroupApplyMapper;

import java.util.List;

public class GroupApplyMapperImpl implements GroupApplyMapper {

    private final String tableName = MongoTableName.group_apply.value();

    @Override
    public GroupApply selectById(String id) {
        return mongo.findByObjectId(tableName, id, GroupApply.class);
    }

    @Override
    public String insert(GroupApply groupApply) {
        return mongo.insertOne(tableName, groupApply);
    }

    @Override
    public List<GroupApply> selectByCondition(GroupApply groupApply) {
        return mongo.findMany(tableName, groupApply, GroupApply.class);
    }

    @Override
    public boolean updateById(GroupApply groupApply) {
        return mongo.updateOneByObjectId(tableName, groupApply, groupApply.getId());
    }

    @Override
    public void deleteById(String id) {

    }
}
