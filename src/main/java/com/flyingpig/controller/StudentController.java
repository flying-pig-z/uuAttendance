package com.flyingpig.controller;

import com.flyingpig.annotations.UserRateLimit;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.common.Result;
import com.flyingpig.service.StudentService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/students")
@Api("与学生信息表相关的api")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @UserRateLimit(value = 2, time = 1)
    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/studentInfo")
    @ApiOperation("学生获取他本人的信息")
    public Result getStudentInfoById(@RequestHeader String Authorization) {
        //设置学生id
        Claims claims = JwtUtil.parseJwt(Authorization);
        Long userId = Long.parseLong(claims.getSubject());
        Map<String, Object> studentInfo = studentService.getStudentInfoByUserId(userId);
        return Result.success(studentInfo);
    }

    //录入用户信息
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("教师添加学生")
    public Result addStudent(@RequestParam String no, @RequestParam String name, @RequestParam(defaultValue = "男") String gender,
                             @RequestParam(defaultValue = "计算机与大数据学院") String colleage) {
        //初始化密码为学号
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(no);
        User user = new User(null, no, password, name, gender, colleage, 1);
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setGrade("20" + no.charAt(2) + no.charAt(3));
        student.setClasS("" + no.charAt(6));
        student.setMajor("计算机与大数据学院/软件学院");
        studentService.addStudent(user, student);
        return Result.success();
    }

}
