package com.flyingpig.controller;

import com.flyingpig.dto.StudentInfo;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.StudentService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @GetMapping("/studentInfo")
    public Result getStudentInfoById(@RequestHeader String Authorization){
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        Map<String,Object> studentInfo=studentService.getStudentInfoByUserId(userId);
        return Result.success(studentInfo);
    }
//    @GetMapping("/getStudentIdByStudentNo/{studentNo}")
//    public Result getIdByNo(@PathVariable String studentNo){
//        Student student=studentService.getByStudentNo(studentNo);
//        Integer studentId=student.getId();
//        return Result.success(studentId);
//    }
}
