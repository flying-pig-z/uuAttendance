package com.flyingpig.dataobject.dto;

import com.flyingpig.dataobject.entity.LeaveApplication;
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
