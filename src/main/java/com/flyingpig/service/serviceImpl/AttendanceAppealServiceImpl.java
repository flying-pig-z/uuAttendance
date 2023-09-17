package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.mapper.*;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.ResultAttendanceAppealDetail;
import com.flyingpig.service.AttendanceAppealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class AttendanceAppealServiceImpl implements AttendanceAppealService {
    @Autowired
    private AttendanceAppealMapper attendanceAppealMapper;
    @Autowired
    private SupervisionTaskMapper supervisionTaskMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseDetailMapper courseDetailMapper;
    @Autowired
    private CourseAttendanceMapper courseAttendanceMapper;
    @Override
    public void addAttendanceAppeal(AttendanceAppeal attendanceAppeal) {
        attendanceAppealMapper.insert(attendanceAppeal);
    }

    @Override
    public List<AttendanceAppealWithCourseName> selectAttendanceAppealByStuUserId(Integer userId) {
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userId);
        Student student=studentMapper.selectOne(studentQueryWrapper);
        QueryWrapper<AttendanceAppeal> attendanceAppealQueryWrapper=new QueryWrapper<>();
        attendanceAppealQueryWrapper.eq("student_id",student.getId());
        List<AttendanceAppeal> AttendanceAppealList=attendanceAppealMapper.selectList(attendanceAppealQueryWrapper);
        List<AttendanceAppealWithCourseName> AttendanceAppealWithCourseNameList=new ArrayList<>();
        for(int i=0;i< AttendanceAppealList.size();i++){
            AttendanceAppeal target=AttendanceAppealList.get(i);
            CourseDetail courseDetail=courseDetailMapper.selectById(target.getCourseId());
            //封装成dto
            AttendanceAppealWithCourseName targetDto=new AttendanceAppealWithCourseName(target,courseDetail.getCourseName());
            AttendanceAppealWithCourseNameList.add(targetDto);
        }
        return AttendanceAppealWithCourseNameList;
    }
    @Override
    public List<AttendanceAppeal> selectLeaveBySupervisionId(Integer SupervisionId) {
        List<AttendanceAppeal> attendanceAppealList = new ArrayList<>();;
        List<Integer> courseList=supervisionTaskMapper.getUnattendancedCourselistByUserId(SupervisionId);
        for(int i=0;i<courseList.size();i++){
            QueryWrapper<AttendanceAppeal> attendanceAppealQueryWrapper=new QueryWrapper<>();
            attendanceAppealQueryWrapper.eq("course_id",courseList.get(i));
            attendanceAppealList.addAll(attendanceAppealMapper.selectList(attendanceAppealQueryWrapper));
        }
        return attendanceAppealList;
    }
    //查询申诉的详情
    @Override
    public ResultAttendanceAppealDetail getAttendanceAppealDetail(Integer attendanceAppealId) {
        //获取本张表数据
        AttendanceAppeal attendanceAppeal=attendanceAppealMapper.selectById(attendanceAppealId);
        //通过外键获取其他表所有数据
        Student student=studentMapper.selectById(attendanceAppeal.getStudentId());
        CourseDetail courseDetail =courseDetailMapper.selectById(attendanceAppeal.getCourseId());
        //封装结果
        ResultAttendanceAppealDetail resultAttendanceAppealDetail=new ResultAttendanceAppealDetail();
        if(attendanceAppeal!=null&&student!=null&& courseDetail !=null){
            resultAttendanceAppealDetail.setBeginTime(attendanceAppeal.getAppealBeginTime());
            resultAttendanceAppealDetail.setEndTime(attendanceAppeal.getAppealEndTime());
            resultAttendanceAppealDetail.setReason(attendanceAppeal.getReason());
            resultAttendanceAppealDetail.setStudentNo(student.getNo());
            resultAttendanceAppealDetail.setStudentName(student.getName());
            resultAttendanceAppealDetail.setCourseName(courseDetail.getCourseName());
        }
        return resultAttendanceAppealDetail;
    }
    //更新申诉状态
//    @Override
//    public void updateAttendanceAppealStatus(Integer attendanceAppealId, String status) {
//        AttendanceAppeal attendanceAppeal=attendanceAppealMapper.selectById(attendanceAppealId);
//        Integer studentId=attendanceAppeal.getStudentId();
//        Integer courseId=attendanceAppeal.getCourseId();
//        if(status.equals("通过"))
//            courseAttendanceMapper.updateStatus(studentId,courseId,"已签到");
//        attendanceAppealMapper.updateAttendanceAppealStatus(attendanceAppealId,status);
//    }




}
