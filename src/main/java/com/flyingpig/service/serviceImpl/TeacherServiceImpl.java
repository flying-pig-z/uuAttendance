package com.flyingpig.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.mapper.TeacherMapper;
import com.flyingpig.entity.Teacher;
import com.flyingpig.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = {Exception.class})
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public Integer getIdByTeacherName(String teacherName) {
        Integer teacherId=teacherMapper.getIdByName(teacherName);
        return teacherId;
    }
    @Override
    public Teacher getTeacherByNo(String username) {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("no",username);
        Teacher target=teacherMapper.selectOne(queryWrapper);
        return target;
    }
}