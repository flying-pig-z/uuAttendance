package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultAttendanceCount {
    Integer attendanceCount;
    Integer absentCount;
    Integer leaveApplicationCount;
    Integer noCheck;
}
