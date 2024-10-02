package com.flyingpig.dataobject.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportExcelModel {
    @ExcelProperty(index = 0)
    private String no;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private String grade;

    @ExcelProperty(index = 3)
    private String clasS;

    @ExcelProperty(index = 4)
    private String gender;

    @ExcelProperty(index = 5)
    private String college;
}
