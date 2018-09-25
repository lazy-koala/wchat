package com.thankjava.wchat.db.mongo;

import com.thankjava.wchat.db.entity.User;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/14
 * @Description:
 **/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过userId查询用户
     * @param username
     * @return
     */
    public User selectByUsername(String username);


}
