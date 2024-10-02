package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.UserRoleRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
    void saveBatch(List<UserRoleRelation> userRoleRelations);
}
