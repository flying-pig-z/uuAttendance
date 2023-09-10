package com.flyingpig.service.serviceImpl;

import com.flyingpig.mapper.CourseAttendanceMapper;
import com.flyingpig.mapper.CourseDetailMapper;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.service.CourseDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class CourseDetailServiceImpl implements CourseDetailService {
    @Autowired
    private CourseDetailMapper courseMapper;
    @Autowired
    private CourseAttendanceMapper courseAttendanceMapper;


    @Override
    public CourseDetail getById(Integer courseId) {
        CourseDetail courseDetail =courseMapper.selectById(courseId);
        return courseDetail;
    }



    @Override
    public CourseDetail getByTime(CourseDetail courseDetail) {
        CourseDetail resultCourseDetail =courseMapper.getByTime(courseDetail);
        return resultCourseDetail;
    }



    @Override
    public Integer getCourseIdByBeginTimeAndTeacherId(String week,String weekday,String section, Integer teacherId) {
        Integer resultcourseId=courseMapper.selectCourseIdByBeginTimeAndTeacherid(week,weekday,section,teacherId);
        return resultcourseId;
    }
}
