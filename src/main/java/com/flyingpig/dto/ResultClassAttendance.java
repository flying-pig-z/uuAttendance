package com.flyingpig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultClassAttendance {
    String studentNo;
    String studentName;
    Integer signedCount;
    Integer unsignedCount;
    Integer leaveCount;
}
