package com.flyingpig.service;

import com.flyingpig.dto.StudentAttendanceNow;
import com.flyingpig.entity.CourseAttendance;
import com.flyingpig.dto.CourseTableInfo;
import com.flyingpig.dto.ResultAttendance;
import com.flyingpig.dto.ResultClassAttendance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CourseAttendanceService {
    List<CourseTableInfo> getCourseTableInfoByWeekAndStudentId(Integer studentId, String week, String year);
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
