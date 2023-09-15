package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.LeaveApplicationWithCourseName;
import com.flyingpig.dataobject.entity.*;
import com.flyingpig.dataobject.dto.ResultLeaveDatail;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/leaves")
@Slf4j
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private StudentService studentService;
    @PostMapping("")
    public Result AddLeave(@RequestBody LeaveApplication leaveApplication,@RequestHeader String Authorization) {
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
        String token=Authorization;
        Claims claims= JwtUtil.parseJwt(token);
        Integer userId=Integer.parseInt(claims.getSubject());
        List<LeaveApplicationWithCourseName> result=leaveService.selectLeaveByUserId(userId);
        return Result.success(result);
    }
    @GetMapping("/supervision")
    public Result selectLeaveBySupervison(@RequestHeader String Authorization){
        //设置督导id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userid=Integer.parseInt(claims.getSubject());
        List<LeaveApplication> leaveApplicationList=leaveService.selectLeaveBySupUserId(userid);
        return Result.success(leaveApplicationList);
    }
    @GetMapping("/{leaveId}/leaveDetail")
    public Result LeaveDetail(@RequestHeader String Authorization, @PathVariable Integer leaveId){
        ResultLeaveDatail resultLeaveDatail=leaveService.getLeaveDetail(leaveId);
        return Result.success(resultLeaveDatail);
    }
//    @PutMapping("/judgeLeave/{leaveId}/{status}")
//    public Result JudgeLeave(@PathVariable Integer leaveId, @PathVariable String status){
//
//        leaveService.updateLeaveStatus(leaveId,status);
//        return Result.success();
//    }
}
