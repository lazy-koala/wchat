package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.GroupInfo;
import com.thankjava.wchat.db.mongo.GroupInfoMapper;

import java.util.List;

public class GroupInfoMapperImpl implements GroupInfoMapper {

    private final String tableName = MongoTableName.group_info.value();


    @Override
    public GroupInfo selectById(String id) {
        return MONGO_MANAGER.findByObjectId(tableName, id, GroupInfo.class);
    }

    @Override
    public String insert(GroupInfo groupInfo) {
        return null;
    }

    @Override
    public List<GroupInfo> selectByCondition(GroupInfo groupInfo) {
        return null;
    }

    @Override
    public boolean updateById(GroupInfo groupInfo) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }
}
