package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveSummary {
    private Integer leaveId;
    private String courseName;
    private String reason;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String status;
}
