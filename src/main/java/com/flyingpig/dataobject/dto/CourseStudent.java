package com.flyingpig.dataobject.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudent {
    private Integer stuUserId;
    private String studentNo;
    private String studentName;
    private Integer studentType;//1为学生，2为督导
}
