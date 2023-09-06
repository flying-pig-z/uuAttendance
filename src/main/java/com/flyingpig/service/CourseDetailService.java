package com.flyingpig.service;

import com.flyingpig.entity.CourseDetail;
import com.flyingpig.dto.CourseTableInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface CourseDetailService {
    public CourseDetail getByTime(CourseDetail courseDetail);

    Integer getCourseIdByBeginTimeAndTeacherId(String week,String weekday,String section,Integer teacherId);

    CourseDetail getById(Integer courseId);
}
