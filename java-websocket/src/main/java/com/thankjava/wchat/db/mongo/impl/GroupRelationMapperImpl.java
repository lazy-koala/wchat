package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.GroupRelation;
import com.thankjava.wchat.db.mongo.GroupRelationMapper;
import org.bson.Document;

import java.util.List;

public class GroupRelationMapperImpl implements GroupRelationMapper {

    private final String tableName = MongoTableName.group_relation.value();

    @Override
    public GroupRelation selectById(String id) {
        return MONGO_MANAGER.findByObjectId(tableName, id, GroupRelation.class);
    }

    @Override
    public String insert(GroupRelation groupRelation) {
        groupRelation.setCreateTime(System.currentTimeMillis());
        return MONGO_MANAGER.insertOne(tableName, groupRelation);
    }

    @Override
    public List<GroupRelation> selectByCondition(GroupRelation groupRelation) {
        return MONGO_MANAGER.findMany(tableName, groupRelation, GroupRelation.class);
    }

    @Override
    public boolean updateById(GroupRelation groupRelation) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<GroupRelation> selectByGroupId(String groupId) {
        return MONGO_MANAGER.findMany(tableName, new Document("groupId", groupId), GroupRelation.class);

    }
}
