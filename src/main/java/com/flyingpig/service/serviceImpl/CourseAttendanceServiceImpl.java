package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.StudentAttendanceNow;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.vo.CourseAttendanceAddVO;
import com.flyingpig.dataobject.vo.SignInVO;
import com.flyingpig.mapper.CourseAttendanceMapper;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.dataobject.dto.CourseTableInfo;
import com.flyingpig.dataobject.dto.ResultAttendance;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.service.CourseAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class CourseAttendanceServiceImpl implements CourseAttendanceService {
    @Autowired
    private CourseAttendanceMapper courseAttendanceMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseDetailMapper courseDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<CourseTableInfo> getCourseTableInfoByWeekAndUserId(Integer userId, Integer week, Integer semester) {
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userId);
        Integer studentId=studentMapper.selectOne(studentQueryWrapper).getId();
        List<CourseTableInfo> result=new ArrayList<>();
        //先筛选出该学生所有考勤信息
        QueryWrapper<CourseAttendance> wrapper =new QueryWrapper<>();
        wrapper.eq("student_id",studentId);
        List<CourseAttendance> courseAttendanceList=courseAttendanceMapper.selectList(wrapper);
        System.out.println(courseAttendanceList);
        //再筛选出对应那一年,那一周的所有考勤信息
        for(CourseAttendance courseAttendance:courseAttendanceList){
            CourseDetail courseDetail=courseDetailMapper.selectById(courseAttendance.getCourseId());
            //获取对应的年和周
            Integer courseSemester=courseDetail.getSemester();
            Integer courseWeek=courseDetail.getWeek();
            //比对筛选
            if(courseWeek.equals(week)&&courseSemester.equals(semester)){
                Integer status=courseAttendance.getStatus();
                QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
                userQueryWrapper.eq("id",courseDetail.getCourseTeacher());
                User user=userMapper.selectOne(userQueryWrapper);
                CourseTableInfo courseDetailDTO=new CourseTableInfo(courseDetail,status,user.getName());
                result.add(courseDetailDTO);
            }
        }
        return result;
    }

    //    @Override
//    public List<CourseDetailWithStatus> getCourseDetailWithStatusByWeek(Integer studentId, String week) {
//        List<CourseDetailWithStatus> resultList=Att
//        return resultList;
//    }
    @Override
    public void updateAttendanceStatus(CourseAttendance attendance){
        attendance.setTime(LocalDateTime.now());
        //更新的条件
        QueryWrapper<CourseAttendance> wrapper=new QueryWrapper<>();
        wrapper.eq("student_id",attendance.getStudentId());
        wrapper.eq("course_id",attendance.getCourseId());
        //如果更新的那条记录存在
        //根据条件查询一条数据，如果结果超过一条会报错
        CourseAttendance select = this.courseAttendanceMapper.selectOne(wrapper);
        if(select!=null){
            //执行更新操作
            int result=this.courseAttendanceMapper.update(attendance,wrapper);
            System.out.println("result = "+result);
        }
    }
    @Override
    public ResultAttendance getWhoNoCheck(Integer courseId) {
        //更新的条件
        QueryWrapper<CourseAttendance> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<CourseAttendance> courseAttendanceList= courseAttendanceMapper.selectList(wrapper);
        CourseAttendance personCourseAttendance=courseAttendanceList.get(0);
        Student student=studentMapper.selectById(personCourseAttendance.getStudentId());
        ResultAttendance resultAttendance=new ResultAttendance();
        resultAttendance.setId(personCourseAttendance.getId());
        resultAttendance.setStudentId(student.getId());
        resultAttendance.setStudentNo(student.getNo());
        resultAttendance.setStudentName(student.getName());
        resultAttendance.setCourseId(courseId);
        resultAttendance.setStatus(personCourseAttendance.getStatus());
        return resultAttendance;
    }

    @Override
    public List<ResultAttendance> getresultAttendanceListByCourseId(Integer courseId) {
        QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper=new QueryWrapper<>();
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id",courseId).eq("status",2);
        List<CourseAttendance> attendanceListAbsence= courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper=new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id",courseId).eq("status",1);
        List<CourseAttendance> attendanceListSigned= courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper=new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id",courseId).eq("status",0);
        List<CourseAttendance> attendanceListUnSigned= courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper=new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id",courseId).eq("status",3);
        List<CourseAttendance> attendanceListLeave= courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        List<CourseAttendance> result=attendanceListAbsence;
        result.addAll(attendanceListLeave);
        result.addAll(attendanceListUnSigned);
        result.addAll(attendanceListSigned);
        List<ResultAttendance> realResult=new ArrayList<>();
        for(int i=0;i<result.size();i++){
            ResultAttendance temp=new ResultAttendance();
            temp.setId(result.get(i).getId());
            temp.setStudentId(result.get(i).getStudentId());
            temp.setCourseId(result.get(i).getCourseId());
            temp.setStatus(result.get(i).getStatus());
            Student student=studentMapper.selectById(result.get(i).getStudentId());
            temp.setStudentName(student.getName());
            temp.setStudentNo(student.getNo());
            realResult.add(temp);
        }
        return realResult;
    }
    //获取这个班级的考勤情况

//    @Override
//    public List<ResultClassAttendance> getClassAttendance(String grade, String major, String Class) {
//        List<Student> studentList=studentMapper.getByGradeAndMajorAndClass(grade,major,Class);
//        List<ResultClassAttendance> resultClassAttendanceList=new ArrayList<>();
//        for(int i=0;i<studentList.size();i++){
//            Integer signedCount= courseAttendanceMapper.countStudentAttendance(studentList.get(i).getId(),"已签到");
//            Integer unsignedCount= courseAttendanceMapper.countStudentAttendance(studentList.get(i).getId(),"未签到");
//            Integer leaveCount= courseAttendanceMapper.countStudentAttendance(studentList.get(i).getId(),"请假");
//            ResultClassAttendance temp=new ResultClassAttendance(studentList.get(i).getNo(),studentList.get(i).getName(),signedCount,unsignedCount,leaveCount);
//            resultClassAttendanceList.add(temp);
//        }
//        return resultClassAttendanceList;
//    }
//    @Override
//    public CourseAttendance getByStudentIdAndBeginTimeAndEndTime(CourseAttendance target) {
//        target= courseAttendanceMapper.getByStudentIdAndBeginTimeAndEndTime(target);
//        return target;
//    }
    @Override
    public CourseTableInfo getAttendanceNowByStuUserId(Integer userId) {
        return null;
    }
    @Override
    public StudentAttendanceNow getStudentAttendanceNow(String studentId) {
        //获取当前时间
        LocalDateTime nowTime=LocalDateTime.now();
        StudentAttendanceNow studentAttendanceNow=new StudentAttendanceNow();
        CourseAttendance courseAttendance=courseAttendanceMapper.getStudentAttendanceNow(studentId);
        studentAttendanceNow.setCourseId(courseAttendance.getCourseId());
        studentAttendanceNow.setStatus(courseAttendance.getStatus());
        CourseDetail courseDetail=courseDetailMapper.selectById(courseAttendance.getCourseId());
        studentAttendanceNow.setCourseName(courseDetail.getCourseName());
        studentAttendanceNow.setLongitude(courseDetail.getLongitude());
        studentAttendanceNow.setLatitude(courseDetail.getLatitude());
        return studentAttendanceNow;
    }

    @Override
    public void addCourseAttendances(CourseAttendanceAddVO courseAttendanceAddVO, String teacherId) {
        CourseAttendance courseAttendance=new CourseAttendance();
        courseAttendance.setTime(LocalDateTime.now());
        courseAttendance.setStatus(0);
        //设置学生id
        QueryWrapper queryWrapper=new QueryWrapper<CourseAttendance>();
        queryWrapper.eq("no",courseAttendanceAddVO.getNo());
        Integer studentId=studentMapper.selectOne(queryWrapper).getId();
        courseAttendance.setStudentId(studentId);
        //设置课程id
        queryWrapper=new QueryWrapper<CourseDetail>();
        queryWrapper.eq("course_teacher",teacherId);
        queryWrapper.eq("course_name",courseAttendanceAddVO.getCourseName());
        queryWrapper.eq("semester",courseAttendanceAddVO.getSemester());
        List<CourseDetail> courseDetailList=courseDetailMapper.selectList(queryWrapper);
        for(CourseDetail courseDetail:courseDetailList){
            courseAttendance.setCourseId(courseDetail.getId());
            courseAttendanceMapper.insert(courseAttendance);
        }
    }

    @Override
    public boolean signIn(String userId, SignInVO signInVO) {
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userId);
        Student student=studentMapper.selectOne(studentQueryWrapper);
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<>();
        courseDetailQueryWrapper.eq("id", signInVO.getCourseId());
        CourseDetail courseDetail=courseDetailMapper.selectOne(courseDetailQueryWrapper);
        if(signInVO.getLatitude().equals(courseDetail.getLatitude())&&signInVO.getLongitude().equals(courseDetail.getLongitude())){
            CourseAttendance courseAttendance=new CourseAttendance();
            courseAttendance.setStatus(1);
            QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper=new QueryWrapper<>();
            courseAttendanceQueryWrapper.eq("course_id",signInVO.getCourseId());
            courseAttendanceMapper.update(courseAttendance,courseAttendanceQueryWrapper);
            return true;
        }else{
            return false;
        }
    }
}
