package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.dto.ClassAttendance;
import com.flyingpig.dataobject.dto.StudentAttendance;
import com.flyingpig.dataobject.entity.CourseAttendance;
import com.flyingpig.dataobject.entity.CourseDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseAttendanceMapper extends BaseMapper<CourseAttendance> {
    CourseAttendance getStudentAttendanceNow(String studentId);

    List<ClassAttendance> listStudentAttendanceByCourseIdList(List<CourseDetail> courseDetailList);

    List<StudentAttendance> getStudentAttendanceByCourseIdAndStudentNo(Integer teaUserid, String courseName, Integer semester, String studentNo);

    List<StudentAttendance> pageStudentAttendanceByCourseIdAndStudentNo(Integer teaUserid, String courseName, Integer semester, String studentNo, int offset, int limit);
}
