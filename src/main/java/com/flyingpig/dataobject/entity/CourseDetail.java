package com.flyingpig.entity;

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
@TableName("course_detail")
public class CourseDetail {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String courseName;
    private Integer semester;
    private Integer week;
    private Integer weekday;
    private Integer sectionStart;
    private Integer sectionEnd;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer courseTeacher;
    private String coursePlace;
    private LocalDateTime schoolOpenTime;
    private String longitude;
    private String latitude;

}
