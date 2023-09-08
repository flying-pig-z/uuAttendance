package com.flyingpig.dto;

import com.flyingpig.entity.CourseDetail;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String place;
    private Integer status;
    private String teacherName;
    public CourseTableInfo(CourseDetail courseDetail, Integer status,String teacherName){
        id=courseDetail.getId();
        name=courseDetail.getCourseName();
        weekday=courseDetail.getWeekday();
        month=courseDetail.getBeginTime().getMonthValue();
        day=courseDetail.getBeginTime().getDayOfMonth();
        sectionStart=courseDetail.getSectionStart();
        sectionEnd=courseDetail.getSectionEnd();
        place=courseDetail.getCoursePlace();
        this.status=status;
        this.teacherName=teacherName;
    }
}
