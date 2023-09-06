package com.flyingpig.service;

import com.flyingpig.entity.Student;
import com.flyingpig.dto.StudentInfo;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface StudentService {

    public Student getStudentByNo(String studentNo);

    Map<String,Object> getStudentInfoByUserId(Integer userid);
}
