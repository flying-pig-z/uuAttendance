package com.flyingpig.service;

import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.LeaveApplication;
import com.flyingpig.dataobject.dto.ResultLeaveDatail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveService {
    public void addLeave(LeaveApplication leaveApplication);
    public List<LeaveApplication> selectLeaveBySupUserId(Integer UserId);

    public ResultLeaveDatail getLeaveDetail(Integer leaveId);


//    public void updateLeaveStatus(Integer leaveId,String status);

    List<LeaveApplicationWithCourseName> selectLeaveByUserId(Integer userId);
}
