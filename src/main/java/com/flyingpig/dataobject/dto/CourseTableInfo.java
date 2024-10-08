package com.flyingpig.dataobject.dto;

import com.flyingpig.dataobject.entity.CourseDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTableInfo {
    private Integer id;
    private String name;
    private Integer weekday;
    private Integer month;
    private Integer day;
    private Integer sectionStart;
    private Integer sectionEnd;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String place;
    private Integer status;
    private String teacherName;

    public CourseTableInfo(CourseDetail courseDetail, Integer status, String teacherName) {
        id = courseDetail.getId();
        name = courseDetail.getCourseName();
        weekday = courseDetail.getWeekday();
        month = courseDetail.getBeginTime().getMonthValue();
        day = courseDetail.getBeginTime().getDayOfMonth();
        sectionStart = courseDetail.getSectionStart();
        sectionEnd = courseDetail.getSectionEnd();
        place = courseDetail.getCoursePlace();
        beginTime = courseDetail.getBeginTime();
        endTime = courseDetail.getEndTime();
        this.status = status;
        this.teacherName = teacherName;
    }
}
