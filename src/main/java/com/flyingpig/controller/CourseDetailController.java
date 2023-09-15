package com.flyingpig.controller;

import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import com.flyingpig.pojo.Result;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/courseDetails")
public class CourseDetailController {
    @Autowired
    CourseDetailService courseDetailService;

    @PostMapping("")
    public Result addCourseDetail(@RequestBody CourseDetailAddVO courseDetailAddVO,@RequestHeader String Authorization){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String teacherId=claims.getSubject();
        System.out.println(courseDetailAddVO);
        courseDetailService.addCourseDetail(teacherId,courseDetailAddVO);
        return Result.success();
    }
//    @GetMapping("teacherGetCourse/{week}/{weekday}/{section}")
//    public Result teacherGetCourse(@RequestHeader String token,@PathVariable String week,@PathVariable String  weekday,@PathVariable String section){
//        Claims claims= JwtUtil.parseJwt(token);
//        String id=claims.get("id").toString();
//        Integer teacherId=Integer.parseInt(id);
//
//        Integer courseId= courseDetailService.getCourseIdByBeginTimeAndTeacherId(week,weekday,section,teacherId);
//        return Result.success(courseId);
//    }
//    @GetMapping("teacherGetCourse/{teacherName}/{week}/{weekday}/{section}")
//    public Result GetCourseByTeacherNameAndWeekAndWeekdayAndSection(@PathVariable String teacherName,@PathVariable String week,@PathVariable String  weekday,@PathVariable String section){
//
//        Integer teacherId=teacherService.getIdByTeacherName(teacherName);
//
//        Integer courseId= courseDetailService.getCourseIdByBeginTimeAndTeacherId(week,weekday,section,teacherId);
//        return Result.success(courseId);
//    }
}
