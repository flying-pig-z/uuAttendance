package com.flyingpig.controller;

import com.flyingpig.dataobject.dto.CourseColumn;
import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import com.flyingpig.common.Result;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@Slf4j
@RestController
@RequestMapping("/courseDetails")
public class CourseDetailController {
    @Autowired
    CourseDetailService courseDetailService;

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    public Result addCourseDetail(@RequestBody CourseDetailAddVO courseDetailAddVO,@RequestHeader String Authorization){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String teacherId=claims.getSubject();
        System.out.println(courseDetailAddVO);
        courseDetailService.addCourseDetail(teacherId,courseDetailAddVO);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('sys:student:operation')")
    @GetMapping("/dataColumn")
    public Result getDataColumn(){
        CourseColumn courseColumn=courseDetailService.getDataColumn();
        return Result.success(courseColumn);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/coursedetailList")
    public Result listCourseDetailByTeaUserIdAndSemester(@RequestHeader String Authorization,@RequestParam String semester){
        Claims claims= JwtUtil.parseJwt(Authorization);
        String teacherId=claims.getSubject();
        HashSet<String> courseNameSet = courseDetailService.listCourseDetailByTeaUserIdAndSemester(teacherId,semester);
        return Result.success(courseNameSet);
    }
}
