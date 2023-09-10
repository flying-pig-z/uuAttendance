package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.CourseAttendance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseAttendanceMapper extends BaseMapper<CourseAttendance> {

    CourseAttendance getStudentAttendanceNow(String studentId);
}
