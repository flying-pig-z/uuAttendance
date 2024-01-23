package com.flyingpig.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flyingpig.dataobject.entity.CourseAttendance;
import com.flyingpig.dataobject.entity.Student;
import com.flyingpig.dataobject.message.SignInMessage;
import com.flyingpig.mapper.CourseAttendanceMapper;
import com.flyingpig.mapper.StudentMapper;
import com.flyingpig.util.AliOSSUtils;
import io.swagger.models.auth.In;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CourseAttendanceHandler {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseAttendanceMapper courseAttendanceMapper;

    @Autowired
    AliOSSUtils aliOSSUtils;

    @RabbitListener(queues = "signIn-queue")
    public void handleSignInRequest(SignInMessage signInMessage) throws IOException {
        String userId=signInMessage.getUserId();
        Integer courseId=signInMessage.getCourseId();
        QueryWrapper<Student> studentQueryWrapper=new QueryWrapper<>();
        studentQueryWrapper.eq("userid",userId);
        Student student=studentMapper.selectOne(studentQueryWrapper);
        CourseAttendance courseAttendance=new CourseAttendance();
        courseAttendance.setStatus(1);
        QueryWrapper<CourseAttendance> courseAttendanceQueryWrapper=new QueryWrapper<>();
        courseAttendanceQueryWrapper.eq("course_id",courseId);
        courseAttendanceQueryWrapper.eq("student_id",student.getId());
        courseAttendanceMapper.update(courseAttendance,courseAttendanceQueryWrapper);
    }
}
