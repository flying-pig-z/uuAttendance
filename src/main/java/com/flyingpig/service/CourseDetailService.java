package com.flyingpig.service;

import com.flyingpig.dataobject.dto.CourseColumn;
import com.flyingpig.dataobject.entity.CourseDetail;
import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public interface CourseDetailService {

    void addCourseDetail(String teacherId, CourseDetailAddVO courseDetailAddVO);

    CourseColumn getDataColumn();

    HashSet<String> listCourseDetailByTeaUserIdAndSemester(String teacherId, String semester);
}
