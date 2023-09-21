package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.LeaveDatail;
import com.flyingpig.pojo.PageBean;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.LeaveService;
import com.flyingpig.service.StudentService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaves")
@Slf4j
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private StudentService studentService;
    @PostMapping("")
    public Result addLeave(@RequestBody LeaveApplication leaveApplication,@RequestHeader String Authorization) {
        //封装完毕后调用service层的add方法

        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userid=Integer.parseInt(claims.getSubject());
        Map<String,Object> studentInfo=studentService.getStudentInfoByUserId(userid);
        leaveApplication.setStatus("0");
        Integer studentId=(Integer) studentInfo.get("id");
        leaveApplication.setStudentId(studentId);
        leaveService.addLeave(leaveApplication);
        return Result.success();
    }
    @GetMapping("/student")
    public Result selectLeaveByStudentId(@RequestHeader String Authorization) {
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        List<LeaveApplicationWithCourseName> result=leaveService.selectLeaveByUserId(userId);
        return Result.success(result);
    }
    @GetMapping("/teaLeaveSummary")
    public Result selectLeaveByTeacherId(@RequestParam Integer pageNo, @RequestParam Integer pageSize,@RequestHeader String Authorization){
        //设置教师id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userid=Integer.parseInt(claims.getSubject());
        PageBean result=leaveService.selectLeaveByTeaUserId(pageNo,pageSize,userid);
        return Result.success(result);
    }
    @GetMapping("/{leaveId}/leaveDetail")
    public Result leaveDetail(@RequestHeader String Authorization, @PathVariable Integer leaveId){
        LeaveDatail resultLeaveDatail=leaveService.getLeaveDetail(leaveId);
        return Result.success(resultLeaveDatail);
    }
    //用于老师通过请假和不通过
    @PutMapping("/{leaveId}")
    public Result judgeLeave(@PathVariable Integer leaveId, @RequestParam String status){
        leaveService.updateLeaveByLeaveIdAndStatus(leaveId,status);
        return Result.success();
    }
}