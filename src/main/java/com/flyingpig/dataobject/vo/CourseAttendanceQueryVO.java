package com.flyingpig.dataobject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAttendanceQueryVO {
    private Integer teaUserId;
    private String courseName;
    private Integer semester;
    private Integer week;
    private Integer weekday;
    private Integer beginSection;
    private Integer endSection;
    private Integer pageNo;
    private Integer pageSize;
}
