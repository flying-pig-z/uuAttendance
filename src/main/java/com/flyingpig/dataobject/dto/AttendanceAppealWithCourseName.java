package com.flyingpig.dto;

import com.flyingpig.entity.AttendanceAppeal;
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
