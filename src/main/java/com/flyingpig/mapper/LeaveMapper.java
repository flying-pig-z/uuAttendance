package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.LeaveApplication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveMapper extends BaseMapper<LeaveApplication> {
    List<LeaveApplication> getByCourseId(Integer courseId);
}
