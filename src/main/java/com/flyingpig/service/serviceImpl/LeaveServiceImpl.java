package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.dto.LeaveSummary;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.mapper.*;
import com.flyingpig.dataobject.dto.LeaveDatail;
import com.flyingpig.pojo.PageBean;
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
    private StudentMapper studentMapper;
    @Autowired
    private CourseDetailMapper courseDetailMapper;
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
            CourseDetail courseDetail=courseDetailMapper.selectById(target.getCourseId());
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
    public PageBean selectLeaveByTeaUserId(Integer pageNo, Integer pageSize,Integer teacherId) {
        List<LeaveApplication> result = new ArrayList<>();
        //先去课程表查询教师所有的教授课程的id，并将课程通过时间由晚到近进行排序,确保最后请假对应的课程由早到晚
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<>();
        courseDetailQueryWrapper.eq("course_teacher",teacherId).orderByDesc("begin_time");
        List<CourseDetail> courseDetailList=courseDetailMapper.selectList(courseDetailQueryWrapper);
        //然后通过教授课程的id分页查询来得到所有请假的信息
        for(CourseDetail courseDetail:courseDetailList){
            QueryWrapper<LeaveApplication> leaveApplicationQueryWrapper=new QueryWrapper<>();
            leaveApplicationQueryWrapper.eq("course_id",courseDetail.getId());
            result.addAll(leaveMapper.selectList(leaveApplicationQueryWrapper));
            System.out.println(leaveMapper.selectList(leaveApplicationQueryWrapper));
        }
        Long count= (long) result.size();
        List<LeaveSummary> resultPage=new ArrayList<>();
        for(int i=(pageNo-1)*pageSize;i<result.size()&&i<pageNo*pageSize;i++){
            LeaveSummary leaveSummary=new LeaveSummary();
            LeaveApplication temp=result.get(i);
            leaveSummary.setLeaveId(temp.getId());
            leaveSummary.setStatus(temp.getStatus());
            leaveSummary.setReason(temp.getReason());
            CourseDetail course=courseDetailMapper.selectById(temp.getCourseId());
            leaveSummary.setCourseName(course.getCourseName());
            leaveSummary.setBeginTime(course.getBeginTime());
            leaveSummary.setEndTime(course.getEndTime());
            resultPage.add(leaveSummary);
        }
        //封装PageBean对象
        PageBean pageBean=new PageBean(count,resultPage);
        return pageBean;
    }
    @Override
    public LeaveDatail getLeaveDetail(Integer leaveId) {
        //获取本张表数据
        LeaveApplication leaveApplication=leaveMapper.selectById(leaveId);
        //通过外键获取其他表所有数据
        Student student=studentMapper.selectById(leaveApplication.getStudentId());
        CourseDetail courseDetail =courseDetailMapper.selectById(leaveApplication.getCourseId());
        //封装结果
        LeaveDatail resultLeaveDatail=new LeaveDatail();

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
