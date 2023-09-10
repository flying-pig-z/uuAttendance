package com.flyingpig.service;

import com.flyingpig.dataobject.dto.StudentAttendanceNow;
import com.flyingpig.dataobject.entity.CourseAttendance;
import com.flyingpig.dataobject.dto.CourseTableInfo;
import com.flyingpig.dataobject.dto.ResultAttendance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CourseAttendanceService {
    List<CourseTableInfo> getCourseTableInfoByWeekAndStudentId(Integer studentId, Integer week, Integer year);
//    public List<CourseDetailWithStatus> getCourseDetailWithStatusByWeek(Integer studentId, String week);

    void updateAttendanceStatus(CourseAttendance attendance);

    Map<String,Object> getWhoNoCheck(Integer courseId);

    List<ResultAttendance> getresultAttendanceListByCourseId(Integer courseId);
    //获取某个班级的考勤情况
//    List<ResultClassAttendance> getClassAttendance(String grade,String major,String Class);

//    CourseAttendance getByStudentIdAndBeginTimeAndEndTime(CourseAttendance target);

    CourseTableInfo getAttendanceNowByStuUserId(Integer userId);

    StudentAttendanceNow getStudentAttendanceNow(String studentId);
}
