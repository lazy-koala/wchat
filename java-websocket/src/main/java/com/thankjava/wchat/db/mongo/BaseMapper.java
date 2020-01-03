package com.thankjava.wchat.db.mongo;


import com.thankjava.toolkit3d.core.db.BasicDBManagerBuilder;
import com.thankjava.toolkit3d.core.db.mongodb.MongoManager;

import java.util.List;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public interface BaseMapper<T> {

    MongoManager MONGO_MANAGER = BasicDBManagerBuilder.buildMongoManager("./config/mongodb.properties");

    T selectById(String id);

    String insert(T t);

    List<T> selectByCondition(T t);

    boolean updateById(T t);

    void deleteById(String id);
}
