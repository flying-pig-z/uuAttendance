package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDatail {
    private String studentNo;
    private String studentName;
    private String courseName;
    private String leavePlace;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String reason;
    private String image;
}
