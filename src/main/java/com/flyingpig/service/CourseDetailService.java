package com.flyingpig.service;

import com.flyingpig.dataobject.dto.CourseColumn;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import org.springframework.stereotype.Service;

@Service
public interface CourseDetailService {
    public CourseDetail getByTime(CourseDetail courseDetail);

    Integer getCourseIdByBeginTimeAndTeacherId(String week,String weekday,String section,Integer teacherId);

    CourseDetail getById(Integer courseId);

    void addCourseDetail(String teacherId, CourseDetailAddVO courseDetailAddVO);

    CourseColumn getDataColumn();
}
