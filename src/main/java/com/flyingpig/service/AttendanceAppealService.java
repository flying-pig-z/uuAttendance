package com.flyingpig.service;

import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.dataobject.entity.AttendanceAppeal;
import com.flyingpig.dataobject.dto.AttendanceAppealDetail;
import com.flyingpig.pojo.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceAppealService {
    void addAttendanceAppeal(AttendanceAppeal attendanceAppeal);
    List<AttendanceAppealWithCourseName> selectAttendanceAppealByStuUserId(Integer id);


    AttendanceAppealDetail getAttendanceAppealDetail(Integer attendanceAppealId);


    void updateByAttendanceAppealIdAndStatus(Integer attendanceAppealId, String status);

    PageBean selectAttendanceAppealSummaryByTeaUserId(Integer pageNo, Integer pageSize, Integer userid);
}
