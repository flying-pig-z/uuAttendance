package com.flyingpig.service;

import org.springframework.stereotype.Service;


@Service
public interface TeacherService {
    Integer getIdByTeacherName(String teacherName);

    Teacher getTeacherByNo(String username);
}