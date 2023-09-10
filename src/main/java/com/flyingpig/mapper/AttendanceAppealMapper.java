package com.flyingpig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.AttendanceAppeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttendanceAppealMapper extends BaseMapper<AttendanceAppeal> {
    void addAttendanceAppeal(AttendanceAppeal attendanceAppeal);
    List<AttendanceAppeal> getByStudentId(Integer studentId);
    //根据课程id进行获取
    List<AttendanceAppeal> getByCourseId(Integer courseId);

    void updateAttendanceAppealStatus(Integer attendanceAppealId, String status);
}
