package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceNow {
    private Integer courseId;
    private Integer status;
    private String courseName;
    private String longitude;
    private String latitude;

    //重写比较方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StudentAttendanceNow other = (StudentAttendanceNow) obj;
        return Objects.equals(courseId, other.courseId) &&
                Objects.equals(status, other.status) &&
                Objects.equals(courseName, other.courseName) &&
                Objects.equals(longitude, other.longitude) &&
                Objects.equals(latitude, other.latitude);
    }
}
