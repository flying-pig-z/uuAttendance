package com.flyingpig.dto;

import com.flyingpig.entity.LeaveApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationWithCourseName {
    LeaveApplication leaveApplication;
    private String courseName;
}
