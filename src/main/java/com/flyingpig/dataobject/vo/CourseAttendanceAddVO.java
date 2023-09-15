package com.flyingpig.dataobject.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAttendanceAddVO {
    private String courseName;
    private Integer semester;
    private String no;
}
