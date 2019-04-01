package com.thankjava.wchat.db.mongo;

import com.thankjava.wchat.db.entity.GroupRelation;

import java.util.List;

public interface GroupRelationMapper extends BaseMapper<GroupRelation> {

    public List<GroupRelation> selectByGroupId(String groupId);

}
