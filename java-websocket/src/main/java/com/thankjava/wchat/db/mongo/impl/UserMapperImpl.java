package com.thankjava.wchat.db.mongo.impl;

import com.thankjava.wchat.consts.MongoTableName;
import com.thankjava.wchat.db.entity.User;
import com.thankjava.wchat.db.mongo.UserMapper;
import org.bson.Document;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public class UserMapperImpl implements UserMapper {

    private final String tableName = MongoTableName.user.value();

    @Override
    public User selectById(String id) {
        return MONGO_MANAGER.findByObjectId(tableName, id, User.class);
    }

    @Override
    public String insert(User user) {
        user.setCreateTime(System.currentTimeMillis());
        return MONGO_MANAGER.insertOne(tableName, user);
    }

    @Override
    public List<User> selectByCondition(User user) {
        return MONGO_MANAGER.findMany(tableName, user, User.class);
    }

    @Override
    public boolean updateById(User user) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public User selectByUsername(String username) {
        return MONGO_MANAGER.findOne(tableName, new Document("username", username), User.class);
    }
}
