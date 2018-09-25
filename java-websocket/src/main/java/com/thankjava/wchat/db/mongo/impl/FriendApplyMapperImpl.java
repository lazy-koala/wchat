package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.FriendApply;
import com.thankjava.wchat.db.mongo.FriendApplyMapper;

import java.util.List;

public class FriendApplyMapperImpl implements FriendApplyMapper {

    private final String tableName = MongoTableName.friend_apply.value();

    @Override
    public FriendApply selectById(String id) {
        return mongo.findByObjectId(tableName, id, FriendApply.class);
    }

    @Override
    public String insert(FriendApply friendApply) {
        friendApply.setCreateTime(System.currentTimeMillis());
        return mongo.insertOne(tableName, friendApply);
    }

    @Override
    public List<FriendApply> selectByCondition(FriendApply friendApply) {
        return mongo.findMany(tableName, friendApply, FriendApply.class);
    }

    @Override
    public boolean updateById(FriendApply friendApply) {
        return mongo.updateOneByObjectId(tableName, friendApply, friendApply.getId());
    }

    @Override
    public void deleteById(String id) {

    }
}
