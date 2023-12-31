package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupervisionTaskWithCourseNameAndBeginTimeAndEndTime {
    private Integer courseId;
    private String courseName;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer semester;
    private Integer weekday;

}
