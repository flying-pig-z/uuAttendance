package com.flyingpig.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface Student_TeacherService {
    Integer selectTeacherByStudent(Integer studentId);
}
