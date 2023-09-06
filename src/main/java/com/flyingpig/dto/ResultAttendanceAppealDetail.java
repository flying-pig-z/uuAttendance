package com.flyingpig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultAttendanceAppealDetail {
    private String studentNo;
    private String studentName;
    private String courseName;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String reason;
}
