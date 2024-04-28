package com.flyingpig.service;

import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface StudentService {

    void addStudent(User user, Student student);

    Map<String,Object> getStudentInfoByUserId(Long userid);
}
