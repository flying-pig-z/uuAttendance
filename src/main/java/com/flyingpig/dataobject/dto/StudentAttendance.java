package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendance {
    private String studentNo;
    private String studentName;
    private Integer week;
    private Integer weekday;
    private Integer status;
}
