package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.entity.CourseDetail;
import com.flyingpig.dto.CourseTableInfo;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CourseDetailMapper extends BaseMapper<CourseDetail> {
    CourseDetail getByTime(CourseDetail courseDetail);
    List<CourseTableInfo> getByTimeNow(Integer studentId, String week);
    CourseTableInfo getOneByTimeNow(Integer studentId, LocalDateTime timeNow);
    Integer selectCourseIdByBeginTimeAndTeacherid(String week,String weekday,String section,Integer teacherId);
}
