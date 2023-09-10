package com.flyingpig.service;

import com.flyingpig.dataobject.entity.Teacher;
import org.springframework.stereotype.Service;


@Service
public interface TeacherService {
    Integer getIdByTeacherName(String teacherName);

    Teacher getTeacherByNo(String username);
}