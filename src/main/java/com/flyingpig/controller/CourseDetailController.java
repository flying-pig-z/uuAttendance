package com.flyingpig.controller;

import com.flyingpig.service.CourseDetailService;
import com.flyingpig.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseDetailController {
    @Autowired
    CourseDetailService courseDetailService;
    @Autowired
    TeacherService teacherService;


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
