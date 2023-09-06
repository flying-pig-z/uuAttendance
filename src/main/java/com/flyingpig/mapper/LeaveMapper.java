package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.entity.LeaveApplication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeaveMapper extends BaseMapper<LeaveApplication> {
    List<LeaveApplication> getByStudentId(Integer studentId);
    List<LeaveApplication> getByCourseId(Integer courseId);
    void updateLeaveStatus(Integer leaveId,String status);
}
