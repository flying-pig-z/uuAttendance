package com.flyingpig.dto;

import com.flyingpig.entity.CourseDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTableInfo {
    private CourseDetail courseDetail;
    private Integer status;
    private String teacherName;
}
