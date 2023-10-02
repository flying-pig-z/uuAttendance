package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassAttendance {
    String studentNo;
    String studentName;
    //签到次数
    Integer signedCount;
    //未签到次数
    Integer nocheckCount;
    //缺勤次数
    Integer absentCount;
    //请假次数
    Integer leaveCount;
}
