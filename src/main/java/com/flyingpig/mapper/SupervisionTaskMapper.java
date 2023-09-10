package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.SupervisionTask;
import com.flyingpig.dataobject.dto.SupervisionTaskWithCourseNameAndBeginTimeAndEndTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SupervisionTaskMapper extends BaseMapper<SupervisionTask> {
    //获取总记录数,表里总的有几行数据
    public Long count() ;
    //获取当前页的结果列表
    List<SupervisionTaskWithCourseNameAndBeginTimeAndEndTime> list(Integer start, Integer pageSize,Integer userId);
    public List<Integer> getUnattendancedCourselistByUserId(Integer studentId);
}
