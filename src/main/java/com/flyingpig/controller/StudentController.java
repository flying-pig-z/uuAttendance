package com.flyingpig.controller;

import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.common.Result;
import com.flyingpig.service.StudentService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/studentInfo")
    public Result getStudentInfoById(@RequestHeader String Authorization){
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        Map<String,Object> studentInfo=studentService.getStudentInfoByUserId(userId);
        return Result.success(studentInfo);
    }

    //录入用户信息
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    public Result addStudent(@RequestParam String no,@RequestParam String name, @RequestParam(defaultValue = "男") String gender,
                             @RequestParam(defaultValue = "计算机与大数据学院") String colleage){
        //初始化密码为学号
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password=passwordEncoder.encode(no);
        User user=new User(null,no,password,name,gender,colleage,1);
        Student student=new Student();
        student.setNo(no);
        student.setName(name);
        student.setGrade("20"+no.charAt(2)+no.charAt(3));
        student.setClasS(""+no.charAt(6));
        student.setMajor("计算机与大数据学院/软件学院");
        studentService.addStudent(user,student);
        return Result.success();
    }

}
