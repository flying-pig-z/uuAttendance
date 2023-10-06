package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.LeaveDatail;
import com.flyingpig.common.PageBean;
import com.flyingpig.common.Result;
import com.flyingpig.service.LeaveService;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/leaves")
@Api("与请假表相关的api")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @PostMapping("")
    @ApiOperation("学生请假")
    public Result addLeave(@RequestBody LeaveApplication leaveApplication, @RequestHeader String Authorization) {
        //封装完毕后调用service层的add方法

        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer userid = Integer.parseInt(claims.getSubject());
        Map<String, Object> studentInfo = studentService.getStudentInfoByUserId(userid);
        leaveApplication.setStatus("0");
        Integer studentId = (Integer) studentInfo.get("id");
        leaveApplication.setStudentId(studentId);
        leaveService.addLeave(leaveApplication);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/student")
    @ApiOperation("学生查看自己的请假列表")
    public Result listLeaveByStudentId(@RequestHeader String Authorization) {
        //设置学生id
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer userId = Integer.parseInt(claims.getSubject());
        List<LeaveApplicationWithCourseName> result = leaveService.listLeaveByUserId(userId);
        return Result.success(result);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/teaLeaveSummary")
    @ApiOperation("教师分页查询学生给他的请假列表")
    public Result pageLeaveByTeacherId(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestHeader String Authorization) {
        //设置教师id
        Claims claims = JwtUtil.parseJwt(Authorization);
        Integer userid = Integer.parseInt(claims.getSubject());
        PageBean result = leaveService.pageLeaveByTeaUserId(pageNo, pageSize, userid);
        return Result.success(result);
    }

    @GetMapping("/{leaveId}/leaveDetail")
    @ApiOperation("学生和教师查看想要查看的请假的详情")
    public Result getleaveDetail(@RequestHeader String Authorization, @PathVariable Integer leaveId) {
        LeaveDatail resultLeaveDatail = leaveService.getLeaveDetail(leaveId);
        return Result.success(resultLeaveDatail);
    }

    //用于老师通过请假和不通过
    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PutMapping("/{leaveId}")
    @ApiOperation("教师判断请假通不通过")
    public Result judgeLeave(@PathVariable Integer leaveId, @RequestParam String status) {
        leaveService.updateLeaveByLeaveIdAndStatus(leaveId, status);
        return Result.success();
    }

}