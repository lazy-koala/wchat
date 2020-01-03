package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.FriendRelation;
import com.thankjava.wchat.db.mongo.FriendRelationMapper;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public class FriendRelationMapperImpl implements FriendRelationMapper {

    private final String tableName = MongoTableName.friend_relation.value();

    @Override
    public FriendRelation selectById(String id) {
        return null;
    }

    @Override
    public String insert(FriendRelation friendRelation) {
        friendRelation.setCreateTime(System.currentTimeMillis());
        return MONGO_MANAGER.insertOne(tableName, friendRelation);
    }

    @Override
    public List<FriendRelation> selectByCondition(FriendRelation friendRelation) {
        return MONGO_MANAGER.findMany(tableName, friendRelation, FriendRelation.class);
    }

    @Override
    public boolean updateById(FriendRelation friendRelation) {
        return false;
    }

    @Override
    public void deleteById(String id) {
        MONGO_MANAGER.deleteOneByObjectId(tableName, id);
    }
}
