package com.flyingpig.dataobject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {
    private Integer id;
    private String no;
    private String name;
    private String gender;
    private String grade;
    private String clasS;
    private String major;
    private String college;
}
