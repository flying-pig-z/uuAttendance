package com.flyingpig.controller;

import com.flyingpig.entity.*;
import com.flyingpig.dto.*;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.CourseAttendanceService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/courseAttendances")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseAttendanceController {
    @Autowired
    private CourseAttendanceService courseAttendanceService;
    @PutMapping ("/status")
    public Result updateAttendanceStatusByStudentIdAndCourseId(@RequestBody CourseAttendance attendance) {
        //调用service层的添加功能
        courseAttendanceService.updateAttendanceStatus(attendance);
        return Result.success();
    }
    @GetMapping("/courseTableInfo")
    public Result getCourseDetailWithStatusByWeekAndStudentId(@RequestParam("week") String week,@RequestParam("semester") String semester,@RequestHeader String Authorization){
        CourseDetail select=new CourseDetail();
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        String id=claims.getSubject();
        Integer StudentId=Integer.parseInt(id);
        List<CourseTableInfo> courseDetailWithStatusList = courseAttendanceService.getCourseTableInfoByWeekAndStudentId(StudentId,week,semester);
        return Result.success(courseDetailWithStatusList);
    }
    //
    @GetMapping("/attendanceNow")
    public Result getAttendanceNowByStuUserId(@RequestHeader String Authorization){
        CourseTableInfo courseDetailWithStatus =new CourseTableInfo();
        //设置学生id
        Claims claims= JwtUtil.parseJwt(Authorization);
        Integer userId=Integer.parseInt(claims.getSubject());
        //设置时间
        // 获取当前时间
        courseDetailWithStatus = courseAttendanceService.getAttendanceNowByStuUserId(userId);
        return Result.success(courseDetailWithStatus);
    }
    @GetMapping("/whoNoCheck")
    public Result getWhoNoCheck(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能
        Map<String,Object> studentInfo=courseAttendanceService.getWhoNoCheck(courseId);
        return Result.success(studentInfo);
    }
    //获取这个课程的学生名单及其这节课的考勤情况
    @GetMapping("")
    public Result getCourseAttendanceBycourseId(@RequestParam("courseId") Integer courseId) {
        //调用service层的添加功能
        List<ResultAttendance> resultAttendanceList= courseAttendanceService.getresultAttendanceListByCourseId(courseId);
        return Result.success(resultAttendanceList);
    }
//    //统计一个课程的总的考勤情况
//    @GetMapping("/Attendance/{courseId}")
//    public Result getAttendanceCount(@PathVariable Integer courseId){
//        //调用service层的添加功能
//        List<ResultAttendance> resultAttendanceList= attendanceService.getCourseAttendance(courseId);
//        Integer attendanceCount=0;
//        Integer absentCount=0;
//        Integer leaveApplicationCount=0;
//        Integer noCheck=0;
//        for(int i=0;i<resultAttendanceList.size();i++){
//            if(resultAttendanceList.get(i).getStatus().equals("已签到")){
//                attendanceCount++;
//            }else if(resultAttendanceList.get(i).getStatus().equals("未签到")){
//                noCheck++;
//            }else if(resultAttendanceList.get(i).getStatus().equals("请假")){
//                absentCount++;
//            }else if(resultAttendanceList.get(i).getStatus().equals("缺勤")){
//                leaveApplicationCount++;
//            }
//        }
//        ResultAttendanceCount resultAttendanceCount=new ResultAttendanceCount();
//        resultAttendanceCount.setAttendanceCount(attendanceCount);
//        resultAttendanceCount.setAbsentCount(absentCount);
//        resultAttendanceCount.setLeaveApplicationCount(leaveApplicationCount);
//        resultAttendanceCount.setNoCheck(noCheck);
//        return Result.success(resultAttendanceCount);
//    }
//    //获取一个班的考勤数据，包括学生学号，姓名，以及签到，未签到以及请假次数的统计
//    @GetMapping("/Attendance/{grade}/{major}/{Class}")
//    public Result getClassAttendance(@PathVariable String grade,@PathVariable String major,@PathVariable String Class){
//        //调用service层的添加功能
//        if(grade.equals("undefined")){
//            grade=null;
//        }
//        if(major.equals("undefined")){
//            major=null;
//        }
//        if(Class.equals("undefined")){
//            Class=null;
//        }
//        List<ResultClassAttendance> resultClassAttendanceList=attendanceService.getClassAttendance(grade,major,Class);
//
//        return Result.success(resultClassAttendanceList);
//    }

}