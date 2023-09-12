package com.flyingpig.dataobject.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailAddVO {
    private String courseName;
    private Integer semester;
    private Integer weekBegin;
    private Integer weekEnd;
    private Integer weekInterval;//1代表单周，2代表双周
    private Integer weekday;
    private Integer sectionStart;
    private Integer sectionEnd;
    private LocalDateTime schoolOpenTime;
    private String coursePlace;
    private String longitude;
    private String latitude;
}
