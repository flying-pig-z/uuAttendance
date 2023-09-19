package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.ResultAttendanceAppealDetail;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.AttendanceAppealService;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/attendanceAppeals")
public class AttendanceAppealController {
    @Autowired
    CourseDetailService courseDetailService;
    @Autowired
    AttendanceAppealService attendanceAppealService;
    @Autowired
    CourseAttendanceService courseAttendanceService;
    @Autowired
    StudentService studentService;
    @PostMapping("")
    public Result addAttendanceAppeal(@RequestBody AttendanceAppeal attendanceAppeal, @RequestHeader String Authorization) {
        //设置userid
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userid=Integer.parseInt(claims.getSubject());
        Map<String,Object> studentInfo=studentService.getStudentInfoByUserId(userid);
        attendanceAppeal.setStatus("0");
        Integer studentId=(Integer) studentInfo.get("id");
        attendanceAppeal.setStudentId(studentId);
        attendanceAppealService.addAttendanceAppeal(attendanceAppeal);
        return Result.success();
    }
    @GetMapping("/student")
    public Result selectAttendanceAppealByStuUserId(@RequestHeader String Authorization) {
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        List<AttendanceAppealWithCourseName> result=attendanceAppealService.selectAttendanceAppealByStuUserId(userId);
        return Result.success(result);
    }
    //查询对应申诉的详细信息
    @GetMapping("/{attendanceAppealId}/attendanceAppealDetail")
    public Result getAttendanceAppealDetail(@RequestHeader String Authorization, @PathVariable Integer attendanceAppealId){
        ResultAttendanceAppealDetail resultAttendanceAppealDetail=attendanceAppealService.getAttendanceAppealDetail(attendanceAppealId);
        return Result.success(resultAttendanceAppealDetail);
    }
    @PutMapping("/{attendanceAppealId}")
    public Result judgeLeave(@PathVariable Integer attendanceAppealId, @RequestParam String status){
        attendanceAppealService.updateByAttendanceAppealIdAndStatus(attendanceAppealId,status);
        return Result.success();
    }

}
