package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.dto.CourseColumn;
import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import com.flyingpig.mapper.CourseAttendanceMapper;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.CourseDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class CourseDetailServiceImpl implements CourseDetailService {
    @Autowired
    private CourseDetailMapper courseDetailMapper;
    @Autowired
    private CourseAttendanceMapper courseAttendanceMapper;

    @Override
    public void addCourseDetail(String teacherId, CourseDetailAddVO courseDetailAddVO) {
        CourseDetail courseDetail=new CourseDetail(courseDetailAddVO);
        courseDetail.setCourseTeacher(Integer.parseInt(teacherId));
        for(int i=courseDetailAddVO.getWeekBegin();i<= courseDetailAddVO.getWeekEnd();i=i+ courseDetailAddVO.getWeekInterval()){
            courseDetail.setWeek(i);
            //计算课程具体时间
            LocalDateTime beginTime=courseDetailAddVO.getSchoolOpenTime().plusWeeks(i-1).plusDays(courseDetailAddVO.getWeekday()-1);
            LocalDateTime endTime=courseDetailAddVO.getSchoolOpenTime().plusWeeks(i-1).plusDays(courseDetailAddVO.getWeekday()-1);
            beginTime = switch (courseDetail.getSectionStart()) {
                case 1 -> beginTime.withHour(8).withMinute(20);
                case 3 -> beginTime.withHour(10).withMinute(20);
                case 5 -> beginTime.withHour(14).withMinute(0);
                case 7 -> beginTime.withHour(15).withMinute(50);
                default -> throw new IllegalArgumentException("Invalid beginning number: " + courseDetail.getSectionStart());
            };
            endTime=switch (courseDetail.getSectionEnd()) {
                case 2 ->  endTime.withHour(10).withMinute(0);
                case 4 ->  endTime.withHour(12).withMinute(0);
                case 6 ->  endTime.withHour(15).withMinute(40);
                case 8 ->  endTime.withHour(17).withMinute(30);
                default -> throw new IllegalArgumentException("Invalid ending number: " + courseDetail.getSectionEnd());
            };
            courseDetail.setBeginTime(beginTime);
            courseDetail.setEndTime(endTime);
            courseDetailMapper.insert(courseDetail);
        }
    }

    @Override
    public CourseColumn getDataColumn() {
        CourseColumn result=new CourseColumn();
        LocalDateTime nowTime=LocalDateTime.now();
        Integer year=nowTime.getYear();
        String semester=new String();
        if(nowTime.getMonth().getValue()>=8||nowTime.getMonth().getValue()<=1){
            semester=year.toString()+"01";
        }else {
            semester=year.toString()+"02";
        }
        result.setSemester(semester);
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<>();
        courseDetailQueryWrapper.eq("semester",semester);
        result.setSchoolOpenTime(courseDetailMapper.selectList(courseDetailQueryWrapper).get(0).getSchoolOpenTime());
        return result;
    }

    @Override
    public HashSet<String> listCourseDetailByTeaUserIdAndSemester(String teacherId, String semester) {
        QueryWrapper<CourseDetail> courseDetailQueryWrapper=new QueryWrapper<>();
        courseDetailQueryWrapper.eq("course_teacher",teacherId)
                .eq("semester",semester)
                .select("course_name");
        List<CourseDetail> courseDetailList=courseDetailMapper.selectList(courseDetailQueryWrapper);
        HashSet<String> courseNameSet=new HashSet<>();
        for(CourseDetail courseDetail:courseDetailList){
            courseNameSet.add(courseDetail.getCourseName());
        }
        return courseNameSet;
    }


}
