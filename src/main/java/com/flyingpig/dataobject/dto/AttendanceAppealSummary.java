package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceAppealSummary {
    private Integer id;
    private String courseName;
    private String reason;
    private String status;
}
