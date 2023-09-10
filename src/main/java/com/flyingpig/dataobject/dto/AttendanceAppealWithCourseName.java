package com.flyingpig.dataobject.dto;

import com.flyingpig.dataobject.entity.AttendanceAppeal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceAppealWithCourseName {
    private AttendanceAppeal attendanceAppeal;
    private String courseName;
}
