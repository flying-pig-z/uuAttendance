package com.flyingpig.controller;

import com.flyingpig.dataobject.constant.StatusCode;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.entity.User;
import com.flyingpig.framework.ratelimiter.annotation.RateLimit;
import com.flyingpig.framework.ratelimiter.annotation.RateLimitKey;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.common.Result;
import com.flyingpig.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/students")
@Api("与学生信息表相关的api")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RateLimit(limitNum = 2, windowSize = "1s")
    @GetMapping("/studentInfo")
    @ApiOperation("学生获取他本人的信息")
    public Result getStudentInfoById(@RateLimitKey @RequestHeader String Authorization) {
        //设置学生id
        Long userId = Long.parseLong(JwtUtil.parseJwt(Authorization).getSubject());
        return Result.success(studentService.getStudentInfoByUserId(userId));
    }


    //录入用户信息
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("教师添加学生")
    public Result addStudent(String no, String name, @RequestParam(defaultValue = "男") String gender,
                             @RequestParam(defaultValue = "计算机与大数据学院") String colleage) {
        //初始化密码为学号
        User user = new User(null, no, new BCryptPasswordEncoder().encode(no), name, gender, colleage, 1);
        Student student = new Student(null, no, name, "20" + no.charAt(2) + no.charAt(3),
                "" + no.charAt(6), "计算机与大数据学院/软件学院", null);
        studentService.addStudent(user, student);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("/excel")
    @ApiOperation("教师导入excel，通过excel添加学生，excel第一行为列名（学号，姓名，性别，学院）,第二行开始为数据")
    public Result addStudentByExcel(MultipartFile studentInfoExcel) {
        if (!judgeExcelFormat(studentInfoExcel)) {
            Result.error(StatusCode.PARAMETERERROR, "文件类型或格式错误");
        }
        studentService.addStudentByExcel(studentInfoExcel);
        return Result.success();
    }


    public boolean judgeExcelFormat(MultipartFile studentInfoExcel) {
        // 获取文件的原始名称
        String originalFilename = studentInfoExcel.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return false;
        }
        // 返回点后的部分作为扩展名
        String fileExtension = originalFilename.substring(lastDotIndex + 1);
        // 检查文件后缀名是否为 "xls" 或 "xlsx"
        if ("xls".equalsIgnoreCase(fileExtension) || "xlsx".equalsIgnoreCase(fileExtension)) {
            return true;
        } else {
            return false;
        }
    }



}
