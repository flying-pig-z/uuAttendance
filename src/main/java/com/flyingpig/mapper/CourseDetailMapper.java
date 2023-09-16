package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.dataobject.dto.CourseTableInfo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CourseDetailMapper extends BaseMapper<CourseDetail> {
    Integer selectCourseIdByBeginTimeAndTeacherid(String week,String weekday,String section,Integer teacherId);
}
