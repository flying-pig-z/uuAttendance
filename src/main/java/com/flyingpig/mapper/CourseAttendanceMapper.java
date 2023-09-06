package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dto.StudentAttendanceNow;
import com.flyingpig.entity.CourseAttendance;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CourseAttendanceMapper extends BaseMapper<CourseAttendance> {

    CourseAttendance getStudentAttendanceNow(String studentId);
}
