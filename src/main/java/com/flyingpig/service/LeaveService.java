package com.flyingpig.service;

import com.flyingpig.dto.LeaveApplicationWithCourseName;
import com.flyingpig.entity.LeaveApplication;
import com.flyingpig.dto.ResultLeaveDatail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LeaveService {
    public void addLeave(LeaveApplication leaveApplication);
    public List<LeaveApplication> selectLeaveBySupUserId(Integer UserId);

    public ResultLeaveDatail getLeaveDetail(Integer leaveId);


//    public void updateLeaveStatus(Integer leaveId,String status);

    List<LeaveApplication> selectLeaveByCounsellorId(Integer counsellorId);

    List<LeaveApplicationWithCourseName> selectLeaveByUserId(Integer userId);
}
