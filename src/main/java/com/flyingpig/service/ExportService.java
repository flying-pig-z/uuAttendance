package com.flyingpig.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

//带出服务
@Service
public interface ExportService {

    void exportStudentAttendance(HttpServletResponse response, Integer teaUserid, String courseName, Integer semester, String studentNo);

    void exportCourseAttendance(HttpServletResponse response, @RequestHeader Integer teaUserid,@RequestParam String courseName, @RequestParam Integer semester,
                                @RequestParam(required = false) Integer week,@RequestParam(required = false) Integer weekday,
                                @RequestParam(required = false) Integer beginSection,@RequestParam(required = false) Integer endSection);
}
