package com.flyingpig.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.dto.StudentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    Student getByUsernameAndPassword(Student student);
    StudentInfo getStudentInfoById(Integer id);
    //通过专业班级年级获取学生id
    List<Student> getByGradeAndMajorAndClass(String grade, String major, String Class);
    List<Student> getByCollege(String major);

    Student getByNo(String studentNo);
}
