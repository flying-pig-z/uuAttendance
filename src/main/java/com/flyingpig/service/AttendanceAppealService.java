package com.flyingpig.service;

import com.flyingpig.dto.AttendanceAppealWithCourseName;
import com.flyingpig.entity.AttendanceAppeal;
import com.flyingpig.dto.ResultAttendanceAppealDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AttendanceAppealService {
    void addAttendanceAppeal(AttendanceAppeal attendanceAppeal);
    List<AttendanceAppealWithCourseName> selectAttendanceAppealByStuUserId(Integer id);

    List<AttendanceAppeal> selectLeaveBySupervisionId(Integer SupervisionId);

    ResultAttendanceAppealDetail getAttendanceAppealDetail(Integer attendanceAppealId);


//    void updateAttendanceAppealStatus(Integer attendanceAppealId, String status);

    List<AttendanceAppeal> selectAttendanceAppealByCounsellorId(Integer counsellorId);


}
