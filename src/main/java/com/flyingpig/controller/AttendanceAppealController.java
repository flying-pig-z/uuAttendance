package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.AttendanceAppealWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.AttendanceAppealDetail;
import com.flyingpig.common.PageBean;
import com.flyingpig.common.Result;
import com.flyingpig.service.AttendanceAppealService;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
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
    @PreAuthorize("hasAuthority('sys:student:operation')")
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
    @PreAuthorize("hasAuthority('sys:student:operation')")
    public Result selectAttendanceAppealByStuUserId(@RequestHeader String Authorization) {
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        List<AttendanceAppealWithCourseName> result=attendanceAppealService.selectAttendanceAppealByStuUserId(userId);
        return Result.success(result);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/teaAttendanceAppealSummary")
    public Result selectAttendanceAppealByTeacherId(@RequestParam Integer pageNo, @RequestParam Integer pageSize,@RequestHeader String Authorization){
        //设置教师id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userid=Integer.parseInt(claims.getSubject());
        PageBean result=attendanceAppealService.selectAttendanceAppealSummaryByTeaUserId(pageNo,pageSize,userid);
        return Result.success(result);
    }

    //查询对应申诉的详细信息
    @GetMapping("/{attendanceAppealId}/attendanceAppealDetail")
    public Result getAttendanceAppealDetail(@RequestHeader String Authorization, @PathVariable Integer attendanceAppealId){
        AttendanceAppealDetail resultAttendanceAppealDetail=attendanceAppealService.getAttendanceAppealDetail(attendanceAppealId);
        return Result.success(resultAttendanceAppealDetail);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PutMapping("/{attendanceAppealId}")
    public Result judgeLeave(@PathVariable Integer attendanceAppealId, @RequestParam String status){
        attendanceAppealService.updateByAttendanceAppealIdAndStatus(attendanceAppealId,status);
        return Result.success();
    }

}
