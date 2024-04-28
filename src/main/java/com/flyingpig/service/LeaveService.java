package com.flyingpig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.LeaveApplication;
import com.flyingpig.dataobject.dto.LeaveDatail;
import com.flyingpig.common.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaveService extends IService<LeaveApplication> {
    void addLeave(LeaveApplication leaveApplication);
    PageBean pageLeaveByTeaUserId(Integer pageNo, Integer pageSize, Integer teacherId);

    LeaveDatail getLeaveDetail(Integer leaveId);


    void updateLeaveByLeaveIdAndStatus(Integer leaveId,String status);

    List<LeaveApplicationWithCourseName> listLeaveByUserId(Integer userId);
}