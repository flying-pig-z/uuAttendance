package com.flyingpig.service;

import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.dataobject.entity.AttendanceAppeal;
import com.flyingpig.dataobject.dto.ResultAttendanceAppealDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceAppealService {
    void addAttendanceAppeal(AttendanceAppeal attendanceAppeal);
    List<AttendanceAppealWithCourseName> selectAttendanceAppealByStuUserId(Integer id);

    List<AttendanceAppeal> selectLeaveBySupervisionId(Integer SupervisionId);

    ResultAttendanceAppealDetail getAttendanceAppealDetail(Integer attendanceAppealId);


//    void updateAttendanceAppealStatus(Integer attendanceAppealId, String status);

}
