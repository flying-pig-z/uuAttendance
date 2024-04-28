package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.common.PageBean;
import com.flyingpig.dataobject.constant.RabbitMQConstants;
import com.flyingpig.dataobject.dto.*;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.message.SignInMessage;
import com.flyingpig.dataobject.vo.CourseAttendanceAddVO;
import com.flyingpig.dataobject.vo.CourseAttendanceQueryVO;
import com.flyingpig.dataobject.vo.SignInVO;
import com.flyingpig.mapper.*;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.util.DistanceCalculator;
import com.flyingpig.util.RedisSafeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.flyingpig.dataobject.constant.RabbitMQConstants.SIGNIN_EXCHANGE_NAME;

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
    @Autowired
    private SupervisionTaskMapper supervisionTaskMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public List<CourseTableInfo> getCourseTableInfoByWeekAndUserId(Integer userId, Integer week, Integer semester) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("userid", userId);
        Integer studentId = studentMapper.selectOne(studentQueryWrapper).getId();
        List<CourseTableInfo> result = new ArrayList<>();
        //先筛选出该学生所有考勤信息
        QueryWrapper<CourseAttendance> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        List<CourseAttendance> courseAttendanceList = courseAttendanceMapper.selectList(wrapper);
        System.out.println(courseAttendanceList);
        //再筛选出对应那一年,那一周的所有考勤信息
        for (CourseAttendance courseAttendance : courseAttendanceList) {
            CourseDetail courseDetail = courseDetailMapper.selectById(courseAttendance.getCourseId());
            //获取对应的年和周
            Integer courseSemester = courseDetail.getSemester();
            Integer courseWeek = courseDetail.getWeek();
            //比对筛选
            if (courseWeek.equals(week) && courseSemester.equals(semester)) {
                Integer status = courseAttendance.getStatus();
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", courseDetail.getCourseTeacher());
                User user = userMapper.selectOne(userQueryWrapper);
                CourseTableInfo courseDetailDTO = new CourseTableInfo(courseDetail, status, user.getName());
                result.add(courseDetailDTO);
            }
        }
        return result;
    }

    @Override
    public void updateAttendanceStatus(CourseAttendance attendance) {
        attendance.setTime(LocalDateTime.now());
        //更新的条件
        QueryWrapper<CourseAttendance> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", attendance.getStudentId());
        wrapper.eq("course_id", attendance.getCourseId());
        //如果更新的那条记录存在
        //根据条件查询一条数据，如果结果超过一条会报错
        CourseAttendance select = this.courseAttendanceMapper.selectOne(wrapper);
        if (select != null) {
            //执行更新操作
            int result = this.courseAttendanceMapper.update(attendance, wrapper);
            System.out.println("result = " + result);
        }
    }

    @Override
    public List<ResultAttendance> listWhoNoCheck(Integer courseId, Integer returneesNumber, List<Integer> existingStudentId) {
        //获取签到学生考勤列表
        QueryWrapper<CourseAttendance> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId).eq("status", 0);
        List<CourseAttendance> allCourseAttendanceList = courseAttendanceMapper.selectList(wrapper);
        //将需要排除学生排除在外
        for (int i = 0; i < existingStudentId.size(); i++) {
            for (int j = 0; j < allCourseAttendanceList.size(); j++) {
                if (allCourseAttendanceList.get(j).getStudentId().equals(existingStudentId.get(i))) {
                    allCourseAttendanceList.remove(j);
                    //因为保证学生不重复，所以break
                    break;
                }
            }
        }
        //获取想要返回个数的还为考勤的学生，不足返回个数的按实际个数来
        List<CourseAttendance> returnCourseAttendanceList = new ArrayList<>();
        for (int i = 0; i < allCourseAttendanceList.size() && i < returneesNumber; i++) {
            returnCourseAttendanceList.add(allCourseAttendanceList.get(i));
        }
        //包装为返回结果
        List<ResultAttendance> resultAttendanceList = new ArrayList<>();//初始化返回结果数组
        for (CourseAttendance personCourseAttendance : returnCourseAttendanceList) {
            //遍历包装
            Student student = studentMapper.selectById(personCourseAttendance.getStudentId());
            ResultAttendance resultAttendance = new ResultAttendance(personCourseAttendance.getId(), student.getId(), student.getNo(), student.getName(),
                    courseId, personCourseAttendance.getStatus());
            resultAttendanceList.add(resultAttendance);
        }
        return resultAttendanceList;
    }

    @Override
    public List<ResultAttendance> getresultAttendanceListByCourseId(Integer courseId) {
        QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id", courseId).eq("status", 2);
        List<CourseAttendance> attendanceListAbsence = courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper = new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id", courseId).eq("status", 1);
        List<CourseAttendance> attendanceListSigned = courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper = new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id", courseId).eq("status", 0);
        List<CourseAttendance> attendanceListUnSigned = courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        courseAttendanceQueryWrapper = new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id", courseId).eq("status", 3);
        List<CourseAttendance> attendanceListLeave = courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        List<CourseAttendance> result = attendanceListAbsence;
        result.addAll(attendanceListLeave);
        result.addAll(attendanceListUnSigned);
        result.addAll(attendanceListSigned);
        List<ResultAttendance> realResult = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            ResultAttendance temp = new ResultAttendance();
            temp.setId(result.get(i).getId());
            temp.setStudentId(result.get(i).getStudentId());
            temp.setCourseId(result.get(i).getCourseId());
            temp.setStatus(result.get(i).getStatus());
            Student student = studentMapper.selectById(result.get(i).getStudentId());
            temp.setStudentName(student.getName());
            temp.setStudentNo(student.getNo());
            realResult.add(temp);
        }
        return realResult;
    }

    @Override
    public StudentAttendanceNow getStudentAttendanceNow(String studentId) {
        //获取当前时间
        LocalDateTime nowTime = LocalDateTime.now();
        StudentAttendanceNow studentAttendanceNow = new StudentAttendanceNow();

        CourseAttendance courseAttendance = courseAttendanceMapper.getStudentAttendanceNow(studentId);
        studentAttendanceNow.setCourseId(courseAttendance.getCourseId());
        studentAttendanceNow.setStatus(courseAttendance.getStatus());
        CourseDetail courseDetail = courseDetailMapper.selectById(courseAttendance.getCourseId());
        studentAttendanceNow.setCourseName(courseDetail.getCourseName());
        studentAttendanceNow.setLongitude(courseDetail.getLongitude());
        studentAttendanceNow.setLatitude(courseDetail.getLatitude());
        return studentAttendanceNow;
    }

    @Override
    public void addCourseAttendances(CourseAttendanceAddVO courseAttendanceAddVO, String teacherId) {
        CourseAttendance courseAttendance = new CourseAttendance();
        courseAttendance.setTime(LocalDateTime.now());
        courseAttendance.setStatus(0);
        //设置学生id
        QueryWrapper queryWrapper = new QueryWrapper<CourseAttendance>();
        queryWrapper.eq("no", courseAttendanceAddVO.getNo());
        Integer studentId = studentMapper.selectOne(queryWrapper).getId();
        courseAttendance.setStudentId(studentId);
        //设置课程id
        queryWrapper = new QueryWrapper<CourseDetail>();
        queryWrapper.eq("course_teacher", teacherId);
        queryWrapper.eq("course_name", courseAttendanceAddVO.getCourseName());
        queryWrapper.eq("semester", courseAttendanceAddVO.getSemester());
        List<CourseDetail> courseDetailList = courseDetailMapper.selectList(queryWrapper);
        for (CourseDetail courseDetail : courseDetailList) {
            courseAttendance.setCourseId(courseDetail.getId());
            courseAttendanceMapper.insert(courseAttendance);
        }
    }

    @Override
    public boolean signIn(String userId, SignInVO signInVO) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("userid", userId);
        Student student = studentMapper.selectOne(studentQueryWrapper);
        QueryWrapper<CourseDetail> courseDetailQueryWrapper = new QueryWrapper<>();
        courseDetailQueryWrapper.eq("id", signInVO.getCourseId());
        CourseDetail courseDetail = courseDetailMapper.selectOne(courseDetailQueryWrapper);
        Double courseLatitude = Double.parseDouble(courseDetail.getLatitude());
        Double courseLongitude = Double.parseDouble(courseDetail.getLongitude());
        if (DistanceCalculator.distanceBetweenCoordinates(signInVO.getLatitude(), signInVO.getLongitude(), courseLatitude, courseLongitude) <= 30) {
            rabbitTemplate.convertAndSend(SIGNIN_EXCHANGE_NAME, "", new SignInMessage(student.getId(), signInVO.getCourseId()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<CourseStudent> listStudentByTeauserIdAndsemesterAndCourseName(String teaUserid, Integer semester, String courseName) {
        //获得其中一节课的id
        QueryWrapper<CourseDetail> courseDetailQueryWrapper = new QueryWrapper<CourseDetail>();
        courseDetailQueryWrapper.eq("course_teacher", teaUserid);
        courseDetailQueryWrapper.eq("course_name", courseName);
        courseDetailQueryWrapper.eq("semester", semester);
        List<CourseDetail> courseDetailList = courseDetailMapper.selectList(courseDetailQueryWrapper);
        Integer courseOneId = courseDetailList.get(0).getId();
        //通过这节课的id去查询考勤表对应的考勤数据
        QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper = new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id", courseOneId).select("student_id");
        List<CourseAttendance> courseAttendanceList = courseAttendanceMapper.selectList(courseAttendanceQueryWrapper);
        List<CourseStudent> courseStudentList = new ArrayList<>();
        //遍历考勤数据的到学生列表，并且结合督导任务表，获取学生身份
        for (CourseAttendance courseAttendance : courseAttendanceList) {
            Integer studentId = courseAttendance.getStudentId();
            Student student = studentMapper.selectById(studentId);
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setStuUserId(student.getUserid());
            courseStudent.setStudentNo(student.getNo());
            courseStudent.setStudentName(student.getName());
            QueryWrapper<SupervisionTask> supervisionTaskQueryWrapper = new QueryWrapper<>();
            supervisionTaskQueryWrapper.eq("course_id", courseOneId).eq("userid", student.getUserid());
            if (supervisionTaskMapper.selectOne(supervisionTaskQueryWrapper) != null) {
                courseStudent.setStudentType(2);
            } else {
                courseStudent.setStudentType(1);
            }
            courseStudentList.add(courseStudent);
        }
        //身份为督导的排在前面
        List<CourseStudent> sortedList = courseStudentList.stream()
                .sorted(Comparator.comparingInt(CourseStudent::getStudentType))
                .collect(Collectors.toList());
        Collections.reverse(sortedList);
        return sortedList;
    }

    @Override
    public PageBean pageCourseAttendance(CourseAttendanceQueryVO courseAttendanceQueryVO) {
        PageBean pageBean = new PageBean();
        QueryWrapper<CourseDetail> courseDetailQueryWrapper = new QueryWrapper<>();
        courseDetailQueryWrapper.eq("course_teacher", courseAttendanceQueryVO.getTeaUserId())
                .eq("course_name", courseAttendanceQueryVO.getCourseName())
                .eq("semester", courseAttendanceQueryVO.getSemester());
        if (courseAttendanceQueryVO.getWeek() != null) {
            courseDetailQueryWrapper.eq("week", courseAttendanceQueryVO.getWeek());
        }
        if (courseAttendanceQueryVO.getWeekday() != null) {
            courseDetailQueryWrapper.eq("weekday", courseAttendanceQueryVO.getWeekday());
        }
        if (courseAttendanceQueryVO.getBeginSection() != null) {
            courseDetailQueryWrapper.eq("section_start", courseAttendanceQueryVO.getBeginSection());
        }
        if (courseAttendanceQueryVO.getEndSection() != null) {
            courseDetailQueryWrapper.eq("section_end", courseAttendanceQueryVO.getEndSection());
        }
        List<CourseDetail> courseDetailList = courseDetailMapper.selectList(courseDetailQueryWrapper);
        List<ClassAttendance> classAttendanceList = courseAttendanceMapper.listStudentAttendanceByCourseIdList(courseDetailList);
        pageBean.setTotal((long) classAttendanceList.size());
        List<ClassAttendance> result = new ArrayList<>();
        for (int i = (courseAttendanceQueryVO.getPageNo() - 1) * courseAttendanceQueryVO.getPageSize(); i < courseAttendanceQueryVO.getPageNo() * courseAttendanceQueryVO.getPageSize() && i < classAttendanceList.size(); i++) {
            result.add(classAttendanceList.get(i));
        }
        pageBean.setRows(result);
        return pageBean;
    }

    @Override
    public PageBean pageStudentAttendanceByteaUserIdAndCourseInfoAndStudentNo(Integer teaUserid, String courseName, Integer semester, String studentNo, Integer pageNo, Integer pageSize) {
        PageBean pageBean = new PageBean();
        List<StudentAttendance> result = courseAttendanceMapper.getStudentAttendanceByCourseIdAndStudentNo(teaUserid, courseName, semester, studentNo);
        pageBean.setTotal((long) result.size());
        //手动分页
        List<StudentAttendance> pageResult = new ArrayList<>();
        for (int i = (pageNo - 1) * pageSize; i < pageNo * pageSize && i < result.size(); i++) {
            pageResult.add(result.get(i));
        }
        pageBean.setRows(pageResult);
        return pageBean;
    }

}
