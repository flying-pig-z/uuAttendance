package com.flyingpig.service;

import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.LeaveApplication;
import com.flyingpig.dataobject.dto.ResultLeaveDatail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveService {
    void addLeave(LeaveApplication leaveApplication);
    List<LeaveApplication> selectLeaveByTeaUserId(Integer UserId);

    ResultLeaveDatail getLeaveDetail(Integer leaveId);


    void updateLeaveByLeaveIdAndStatus(Integer leaveId,String status);

    List<LeaveApplicationWithCourseName> selectLeaveByUserId(Integer userId);
}