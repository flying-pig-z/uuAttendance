package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.AttendanceAppealSummary;
import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.mapper.*;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.AttendanceAppealDetail;
import com.flyingpig.common.PageBean;
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
    public List<AttendanceAppealWithCourseName> listAttendanceAppealByStuUserId(Integer userId) {
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
    //查询申诉的详情
    @Override
    public AttendanceAppealDetail getAttendanceAppealDetail(Integer attendanceAppealId) {
        //获取本张表数据
        AttendanceAppeal attendanceAppeal=attendanceAppealMapper.selectById(attendanceAppealId);
        //通过外键获取其他表所有数据
        Student student=studentMapper.selectById(attendanceAppeal.getStudentId());
        CourseDetail courseDetail =courseDetailMapper.selectById(attendanceAppeal.getCourseId());
        //封装结果
        AttendanceAppealDetail resultAttendanceAppealDetail=new AttendanceAppealDetail();
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

    @Override
    public void updateByAttendanceAppealIdAndStatus(Integer attendanceAppealId, String status) {
        //先将考勤申诉表中的考勤申诉状态改为通过
        AttendanceAppeal attendanceAppeal=new AttendanceAppeal();
        attendanceAppeal.setId(attendanceAppealId);
        attendanceAppeal.setStatus(status);
        attendanceAppealMapper.updateById(attendanceAppeal);
        //如果通过再将签到表中的签到状态改为请假
        if(status.equals("1")){
            AttendanceAppeal attendanceAppeal1=attendanceAppealMapper.selectById(attendanceAppealId);
            Integer courseId=attendanceAppeal1.getCourseId();
            Integer studentId=attendanceAppeal1.getStudentId();
            CourseAttendance courseAttendance=new CourseAttendance();
            QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper=new QueryWrapper<>();
            courseAttendanceQueryWrapper.eq("course_id",courseId);
            courseAttendanceQueryWrapper.eq("student_id",studentId);
            courseAttendance.setStatus(1);
            courseAttendanceMapper.update(courseAttendance,courseAttendanceQueryWrapper);
        }
    }
    @Override
    public PageBean pageAttendanceAppealSummaryByTeaUserId(Integer pageNo, Integer pageSize, Integer teacherId) {
        List<AttendanceAppeal> result = new ArrayList<>();
        //先去课程表查询教师所有的教授课程的id，并将课程通过时间由晚到近进行排序,确保最后请假对应的课程由早到晚
        QueryWrapper<CourseDetail> courseDetailQueryWrapper = new QueryWrapper<>();
        courseDetailQueryWrapper.eq("course_teacher", teacherId).orderByDesc("begin_time");
        List<CourseDetail> courseDetailList = courseDetailMapper.selectList(courseDetailQueryWrapper);
        //然后通过教授课程的id分页查询来得到所有考勤申诉的信息
        for (CourseDetail courseDetail : courseDetailList) {
            QueryWrapper<AttendanceAppeal> attendanceAppealQueryWrapper = new QueryWrapper<>();
            attendanceAppealQueryWrapper.eq("course_id", courseDetail.getId());
            result.addAll(attendanceAppealMapper.selectList(attendanceAppealQueryWrapper));
            System.out.println(attendanceAppealMapper.selectList(attendanceAppealQueryWrapper));
        }
        Long count = (long) result.size();
        List<AttendanceAppealSummary> resultPage = new ArrayList<>();
        for (int i = (pageNo - 1) * pageSize; i < result.size() && i < pageNo * pageSize; i++) {
            AttendanceAppealSummary attendanceAppealSummary = new AttendanceAppealSummary();
            AttendanceAppeal temp = result.get(i);
            attendanceAppealSummary.setId(temp.getId());
            attendanceAppealSummary.setStatus(temp.getStatus());
            attendanceAppealSummary.setReason(temp.getReason());
            CourseDetail course = courseDetailMapper.selectById(temp.getCourseId());
            attendanceAppealSummary.setCourseName(course.getCourseName());
            resultPage.add(attendanceAppealSummary);
        }
        //封装PageBean对象
        PageBean pageBean=new PageBean(count,resultPage);
        return pageBean;
    }
}
