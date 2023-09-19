package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.mapper.*;
import com.flyingpig.dataobject.dto.ResultLeaveDatail;
import com.flyingpig.service.LeaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private SupervisionTaskMapper supervisionTaskMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseDetailMapper courseMapper;
    @Autowired
    private CourseAttendanceMapper courseAttendanceMapper;

    @Override
    public List<LeaveApplicationWithCourseName> selectLeaveByUserId(Integer userId) {
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userId);
        Student student=studentMapper.selectOne(studentQueryWrapper);
        QueryWrapper<LeaveApplication> leaveApplicationQueryWrapper=new QueryWrapper<>();
        leaveApplicationQueryWrapper.eq("student_id",student.getId());
        List<LeaveApplication> leaveApplicationList=leaveMapper.selectList(leaveApplicationQueryWrapper);
        List<LeaveApplicationWithCourseName> leaveApplicationWithCourseNameList=new ArrayList<>();
        for(int i=0;i< leaveApplicationList.size();i++){
            LeaveApplication target=leaveApplicationList.get(i);
            CourseDetail courseDetail=courseMapper.selectById(target.getCourseId());
            //封装成dto
            LeaveApplicationWithCourseName targetDto=new LeaveApplicationWithCourseName(target,courseDetail.getCourseName());
            leaveApplicationWithCourseNameList.add(targetDto);
        }
        return leaveApplicationWithCourseNameList;
    }
    @Override
    public void addLeave(LeaveApplication leaveApplication) {
        leaveMapper.insert(leaveApplication);
    }
    //根据督导id获取督导对应的请假
    @Override
    public List<LeaveApplication> selectLeaveByTeaUserId(Integer SupervisionId) {
        List<LeaveApplication> leaveApplicationList = new ArrayList<>();
        List<Integer> courseList=supervisionTaskMapper.getUnattendancedCourselistByUserId(SupervisionId);
        for(int i=0;i<courseList.size();i++){
            QueryWrapper<LeaveApplication> leaveApplicationQueryWrapper=new QueryWrapper<>();
            leaveApplicationQueryWrapper.eq("course_id",courseList.get(i));
            leaveApplicationList.addAll(leaveMapper.selectList(leaveApplicationQueryWrapper));
        }
        return leaveApplicationList;
    }

    @Override
    public ResultLeaveDatail getLeaveDetail(Integer leaveId) {
        //获取本张表数据
        LeaveApplication leaveApplication=leaveMapper.selectById(leaveId);
        //通过外键获取其他表所有数据
        Student student=studentMapper.selectById(leaveApplication.getStudentId());
        CourseDetail courseDetail =courseMapper.selectById(leaveApplication.getCourseId());
        //封装结果
        ResultLeaveDatail resultLeaveDatail=new ResultLeaveDatail();

        if(leaveApplication!=null&&student!=null&& courseDetail !=null){
            resultLeaveDatail.setLeaveId(leaveId);
            resultLeaveDatail.setBeginTime(leaveApplication.getAppealBeginTime());
            resultLeaveDatail.setEndTime(leaveApplication.getAppealEndTime());
            resultLeaveDatail.setReason(leaveApplication.getReason());
            resultLeaveDatail.setStudentNo(student.getNo());
            resultLeaveDatail.setStudentName(student.getName());
            resultLeaveDatail.setCourseName(courseDetail.getCourseName());
        }
        return resultLeaveDatail;
    }

    @Override
    public void updateLeaveByLeaveIdAndStatus(Integer leaveId,String status) {
        //先将请假表中的请假状态改为通过
        LeaveApplication leaveApplication=new LeaveApplication();
        leaveApplication.setId(leaveId);
        leaveApplication.setStatus(status);
        leaveMapper.updateById(leaveApplication);
        //如果通过再将签到表中的签到状态改为请假
        if(status.equals("1")){
            LeaveApplication leaveApplication1=leaveMapper.selectById(leaveId);
            Integer courseId=leaveApplication1.getCourseId();
            Integer studentId=leaveApplication1.getStudentId();
            CourseAttendance courseAttendance=new CourseAttendance();
            QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper=new QueryWrapper<>();
            courseAttendanceQueryWrapper.eq("course_id",courseId);
            courseAttendanceQueryWrapper.eq("student_id",studentId);
            courseAttendance.setStatus(3);
            courseAttendanceMapper.update(courseAttendance,courseAttendanceQueryWrapper);
        }
    }


}
