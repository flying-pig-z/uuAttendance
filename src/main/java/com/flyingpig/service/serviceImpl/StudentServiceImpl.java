package com.flyingpig.service.serviceImpl;

import cn.hutool.cache.impl.CacheValuesIterator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.entity.UserRoleRelation;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.mapper.UserMapper;
import com.flyingpig.mapper.UserRoleRelationMapper;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.cache.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.flyingpig.dataobject.entity.User;

import java.util.HashMap;
import java.util.Map;

import static com.flyingpig.dataobject.constant.RedisConstants.USER_INFO_KEY;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    CacheUtil redisSafeUtil;
    @Autowired
    CacheUtil cacheUtil;

    @Override
    public void addStudent(User user, Student student) {
        userMapper.insert(user);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("no", student.getNo());
        Integer userId = userMapper.selectOne(userQueryWrapper).getId();
        student.setUserid(userId);
        studentMapper.insert(student);
        //插入关系表中，赋予学生身份和权限
        UserRoleRelation userRoleRelation = new UserRoleRelation(userId, 1);
        userRoleRelationMapper.insert(userRoleRelation);
    }

    @Override
    public Map<String, Object> getStudentInfoByUserId(Long userid) {
        Map<String, Object> target = new HashMap<>();
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("userid", userid);
        Student student = studentMapper.selectOne(studentQueryWrapper);
//        User user = userMapper.selectById(userid);
        User user = cacheUtil.get(USER_INFO_KEY + userid, User.class);
        target.put("id", student.getId());
        target.put("no", user.getNo());
        target.put("name", user.getName());
        target.put("gender", user.getGender());
        target.put("grade", student.getGrade());
        target.put("class", student.getClasS());
        target.put("major", student.getMajor());
        target.put("college", user.getCollege());
        return target;
    }

}
