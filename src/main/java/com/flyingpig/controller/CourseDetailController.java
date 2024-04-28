package com.flyingpig.controller;

import com.flyingpig.common.Result;
import com.flyingpig.dataobject.dto.CourseColumn;
import com.flyingpig.dataobject.vo.CourseDetailAddVO;
import com.flyingpig.service.CourseDetailService;
import com.flyingpig.util.JwtUtil;
import com.flyingpig.util.RedisSafeUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@Slf4j
@RestController
@RequestMapping("/courseDetails")
@Api("与课程详情表相关的api")
public class CourseDetailController {
    @Autowired
    CourseDetailService courseDetailService;

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @PostMapping("")
    @ApiOperation("教师添加课程信息")
    public Result addCourseDetail(@RequestBody CourseDetailAddVO courseDetailAddVO, @RequestHeader String Authorization) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String teacherId = claims.getSubject();
        System.out.println(courseDetailAddVO);
        courseDetailService.addCourseDetail(teacherId, courseDetailAddVO);
        return Result.success();
    }

    @PreAuthorize("hasAnyAuthority('sys:student:operation','sys:teacher:operation')")
    @GetMapping("/dataColumn")
    @ApiOperation("学生和教师获取当前的学期和学校开学时间")
    public Result getDataColumn() {
        CourseColumn courseColumn = courseDetailService.getDataColumn();
        return Result.success(courseColumn);
    }

    @PreAuthorize("hasAuthority('sys:teacher:operation')")
    @GetMapping("/coursedetailList")
    @ApiOperation("下拉框通过学期查询课程列表")
    public Result listCourseDetailByTeaUserIdAndSemester(@RequestHeader String Authorization, @RequestParam String semester) {
        Claims claims = JwtUtil.parseJwt(Authorization);
        String teacherId = claims.getSubject();
        HashSet<String> courseNameSet = courseDetailService.listCourseDetailByTeaUserIdAndSemester(teacherId, semester);
        return Result.success(courseNameSet);
    }
}
