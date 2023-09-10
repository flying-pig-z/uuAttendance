package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flyingpig.dataobject.entity.User;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void addStudent(User user, Student student) {
        Integer userId=userMapper.insert(user);
        student.setUserid(userId);
        studentMapper.insert(student);
    }
    @Override
    public Map<String,Object> getStudentInfoByUserId(Integer userid) {
        Map<String,Object> target=new HashMap<>();
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userid);
        Student student=studentMapper.selectOne(studentQueryWrapper);
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("id",userid);
        User user=userMapper.selectOne(userQueryWrapper);
        target.put("id",student.getId());
        target.put("no",user.getNo());
        target.put("name",user.getName());
        target.put("gender",user.getGender());
        target.put("grade",student.getGrade());
        target.put("class",student.getClasS());
        target.put("major",student.getMajor());
        target.put("college",user.getCollege());
        return target;
    }

}
