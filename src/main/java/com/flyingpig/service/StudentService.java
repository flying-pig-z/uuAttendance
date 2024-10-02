package com.flyingpig.service;

import com.flyingpig.dataobject.dto.StudentImportExcelModel;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Service
public interface StudentService {

    void addStudent(User user, Student student);

    Map<String,Object> getStudentInfoByUserId(Long userid);

    void addStudentByExcel(MultipartFile studentInfoExcel);

    void batchInsert(List<StudentImportExcelModel> batch);
}
