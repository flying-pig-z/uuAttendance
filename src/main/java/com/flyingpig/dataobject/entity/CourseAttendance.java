package com.flyingpig.dataobject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("course_attendance")
public class CourseAttendance {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private LocalDateTime time;
    private Integer status;
}
